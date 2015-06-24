package hudson.plugins.violations;

import static java.util.logging.Level.SEVERE;
import hudson.model.HealthReport;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Project;
/** cannot be used in a slave
 import hudson.maven.MavenModule;
 /**/
import hudson.plugins.violations.hudson.AbstractViolationsBuildAction;
/** cannot be used in a slave
 import hudson.plugins.violations.hudson.maven.*;
 /**/
import hudson.plugins.violations.model.BuildModel;
import hudson.plugins.violations.model.FileModel;
import hudson.plugins.violations.model.Suppression;
import hudson.plugins.violations.parse.BuildModelParser;
import hudson.plugins.violations.parse.ParseXML;
import hudson.plugins.violations.render.FileModelProxy;
import hudson.plugins.violations.render.NoViolationsFile;
import hudson.plugins.violations.util.HelpHudson;
import hudson.plugins.violations.util.RecurDynamic;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * This contains the report for the violations of a particular build.
 */
public class ViolationsReport implements Serializable {
    private static final Logger LOG = Logger.getLogger(ViolationsReport.class.getName());

    private AbstractBuild<?, ?> build;
    private ViolationsConfig config;
    private final Map<String, Integer> violations = new TreeMap<String, Integer>();
    private final Map<String, TypeSummary> typeSummaries = new TreeMap<String, TypeSummary>();

    private transient WeakReference<BuildModel> modelReference;

    /**
     * Set the build.
     *
     * @param build
     *            the current build.
     */
    public void setBuild(AbstractBuild<?, ?> build) {
        this.build = build;
    }

    /**
     * Get the build.
     *
     * @return the build.
     */
    public AbstractBuild<?, ?> getBuild() {
        return build;
    }

    /**
     * Set the config.
     *
     * @param config
     *            the config.
     */
    public void setConfig(ViolationsConfig config) {
        this.config = config;
    }

    /**
     * Get the config.
     *
     * @return the config.
     */
    public ViolationsConfig getConfig() {
        return config;
    }

    /**
     * Get the violation counts for the build.
     *
     * @return a map of type to count.
     */
    public Map<String, Integer> getViolations() {
        return violations;
    }

    /**
     * Get the overall health for the build.
     *
     * @return the health report, null if there are no counts.
     */
    public HealthReport getBuildHealth() {
        List<HealthReport> reports = getBuildHealths();
        HealthReport ret = null;
        for (HealthReport report : reports) {
            ret = HealthReport.min(ret, report);
        }
        return ret;
    }

    /**
     * Get a health report for each type.
     *
     * @return a list of health reports.
     */
    public List<HealthReport> getBuildHealths() {
        List<HealthReport> ret = new ArrayList<HealthReport>();
        for (String type : config.getTypeConfigs().keySet()) {
            HealthReport health = getHealthReportFor(type);
            if (health != null) {
                ret.add(health);
            }
        }
        return ret;
    }

    /**
     * Get the health for a particulat type.
     *
     * @param type
     *            the type to get the health for.
     * @return the health report.
     */
    public HealthReport getHealthReportFor(String type) {
        Integer count = violations.get(type);
        if (count == null || config.getTypeConfigs() == null) {
            return null;
        }
        int h = config.getTypeConfigs().get(type).getHealthFor(count);
        if (h < 0) {
            return new HealthReport(0, Messages._ViolationsReport_NoReport(type));
        } else {
            return new HealthReport(h, Messages._ViolationsReport_ViolationsCount(type, count));
        }
    }

    /**
     * Get the detailed model for the build. This is lazily build from an xml
     * created during publisher action.
     *
     * @return the build model.
     */
    public BuildModel getModel() {
        BuildModel model = null;
        if (modelReference != null) {
            model = modelReference.get();
            if (model != null) {
                return model;
            }
        }

        File xmlFile = new File(build.getRootDir(), MagicNames.VIOLATIONS + "/" + MagicNames.VIOLATIONS + ".xml");
        try {
            model = new BuildModel(xmlFile);
            ParseXML.parse(xmlFile, new BuildModelParser().buildModel(model));
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Unable to parse " + xmlFile, ex);
            return null;
        }

        modelReference = new WeakReference<BuildModel>(model);
        return model;
    }

    /**
     * Get the file model proxt for a file name.
     *
     * @param name
     *            the name to use.
     * @return the file model proxy.
     */
    public FileModelProxy getFileModelProxy(String name) {
        BuildModel model = getModel();
        if (model == null) {
            return null;
        }
        return model.getFileModelMap().get(name);
    }

    /**
     * This gets called to display a particular violation file report.
     *
     * @param token
     *            the current token in the path being parsed.
     * @param req
     *            the http/stapler request.
     * @param rsp
     *            the http/stapler response.
     * @return an object to handle the token.
     */
    public Object getDynamic(String token, StaplerRequest req, StaplerResponse rsp) {
        // System.out.println("LOOKING for " + req.getRestOfPath());
        String name = req.getRestOfPath();
        if (name.equals("")) {
            return null;
        }
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        FileModelProxy proxy = getFileModelProxy(name);
        if (proxy != null) {
            return new RecurDynamic("", name, proxy.build(build).contextPath(""));
        } else {
            return new RecurDynamic("", name, new NoViolationsFile(name, build));
        }
    }

    /**
     * get the configuration for this job.
     *
     * @return the configuration of the job.
     */
    public ViolationsConfig getLiveConfig() {
        AbstractProject<?, ?> abstractProject = build.getProject();
        if (abstractProject instanceof Project) {
            Project project = (Project) abstractProject;
            ViolationsPublisher publisher = (ViolationsPublisher) project.getPublisher(ViolationsPublisher.DESCRIPTOR);
            return publisher == null ? null : publisher.getConfig();
        }
        return null;
    }

    /**
     * Add a suppression to the set of suppressions.
     *
     * @param suppression
     *            the suppression to add.
     */
    public void addSuppression(Suppression suppression) throws IOException {
        ViolationsConfig config = getLiveConfig();
        if (config != null) {
            config.getSuppressions().add(suppression);
            ((AbstractProject) build.getParent()).save();
        }
    }

    /**
     * Remove a suppression to the set of suppressions.
     *
     * @param suppression
     *            the suppression to remove.
     */
    public void removeSuppression(Suppression suppression) throws IOException {
        ViolationsConfig config = getLiveConfig();
        if (config != null) {
            config.getSuppressions().remove(suppression);
            ((AbstractProject) build.getParent()).save();
        }
    }

    /**
     * Get a map of type to type summary report.
     *
     * @return a map.
     */
    public Map<String, TypeSummary> getTypeSummaries() {
        return typeSummaries;
    }

    /**
     * Get a type summary for a particular type.
     *
     * @param type
     *            the violation type.
     * @return the type summary.
     */
    public TypeSummary getTypeSummary(String type) {
        TypeSummary ret = typeSummaries.get(type);
        if (ret == null) {
            ret = new TypeSummary();
            typeSummaries.put(type, ret);
        }
        return ret;
    }

    /**
     * Get a map of type to type reports.
     *
     * @return a map of type to type reports.
     */
    public Map<String, TypeReport> getTypeReports() {
        Map<String, TypeReport> ret = new TreeMap<String, TypeReport>();
        for (String t : violations.keySet()) {
            int c = violations.get(t);
            HealthReport health = getHealthReportFor(t);
            ret.put(t, new TypeReport(t, health.getIconUrl(), c));
        }
        return ret;
    }

    /**
     * Graph this report. Note that for some reason, yet unknown, hudson seems
     * to pick an in memory ViolationsReport object and not the report for the
     * build. (Reason may be related to the fact that serialized builds may not
     * be the same as in-memory builds). Need to find the correct build from the
     * URI.
     *
     * @param req
     *            the request paramters
     * @param rsp
     *            the response.
     * @throws IOException
     *             if there is an error writing the graph.
     */
    public void doGraph(StaplerRequest req, StaplerResponse rsp) throws IOException {
        AbstractBuild<?, ?> tBuild = build;
        int buildNumber = HelpHudson.findBuildNumber(req);
        if (buildNumber != 0) {
            tBuild = build.getParent().getBuildByNumber(buildNumber);
            if (tBuild == null) {
                tBuild = build;
            }
        }

        AbstractViolationsBuildAction r = tBuild.getAction(AbstractViolationsBuildAction.class);
        if (r == null) {
            return;
        }
        r.doGraph(req, rsp);
    }

    /**
     * Get the string number for a particular type.
     *
     * @param t
     *            the type
     * @return the string - a number for a value type, "" for not found and
     *         "No reports" for < 0.
     */
    public String getNumberString(String t) {
        Integer v = violations.get(t);
        if (v == null) {
            return "";
        }
        if (v < 0) {
            return "<span style='color:red'>No reports</span>";
        }
        return "" + v;
    }

    /**
     * Get the icon for a type.
     *
     * @param t
     *            the type
     * @return the icon name.
     */
    public String getIcon(String t) {
        violations.get(t);
        HealthReport h = getHealthReportFor(t);
        if (h == null) {
            return null;
        }
        return h.getIconUrl();
    }

    /**
     * Report class for a particular type.
     */
    public static class TypeReport {
        private final String type;
        private final String icon;
        private final int number;

        /**
         * Create the report class for a type.
         *
         * @param type
         *            the violation type.
         * @param icon
         *            the health icon to display.
         * @param number
         *            the number of violations.
         */
        public TypeReport(String type, String icon, int number) {
            this.type = type;
            this.icon = icon;
            this.number = number;
        }

        /**
         * Get the violation type.
         *
         * @return the violation type.
         */
        public String getType() {
            return type;
        }

        /**
         * Get the health icon to display.
         *
         * @return the health icon.
         */
        public String getIcon() {
            return icon;
        }

        /**
         * Get the number of violations.
         *
         * @return the number.
         */
        public int getNumber() {
            return number;
        }

        /**
         * Get the number of violations as a string.
         *
         * @return the number if >= 0 othersise an error string.
         */
        public String getNumberString() {
            if (number >= 0) {
                return "" + number;
            } else {
                return "<span style='color:red'>No reports</span>";
            }
        }

    }

    /**
     * Get the previous ViolationsReport
     *
     * @return the previous report if present, null otherwise.
     */
    public ViolationsReport previous() {
        return findViolationsReport(build.getPreviousBuild());
    }

    /**
     * Get the number of violations for a particular type.
     *
     * @param type
     *            the violation type.
     * @return the number of violations.
     */
    public int typeCount(String type) {
        if (getModel() == null) {
            return 0;
        }
        return getModel().getTypeCountMap().get(type).getCount();
    }

    /**
     * Get the number of files in violation for a particular type.
     *
     * @param type
     *            the violation type.
     * @return the number of files.
     */
    public int fileCount(String type) {
        if (getModel() == null) {
            return 0;
        }
        return getModel().getTypeCountMap().get(type).getNumberFiles();
    }

    /**
     * Get the number of violations of a type for a file.
     *
     * @param type
     *            the type in question.
     * @param filename
     *            the name of the file.
     * @return the number found (0 for none and for failures).
     */
    public int violationCount(String type, String filename) {
        FileModelProxy proxy = getFileModelProxy(filename);
        if (proxy == null) {
            return 0;
        }
        FileModel fileModel = proxy.getFileModel();
        if (fileModel == null) {
            return 0;
        }
        FileModel.LimitType limit = fileModel.getLimitTypeMap().get(type);
        if (limit == null) {
            return 0;
        }
        return limit.getNumber();
    }

    /**
     * Get the unstable status for this report.
     *
     * @return true if one of the violations equals or exceed the unstable
     *         threshold for that violations type.
     */
    private boolean isUnstable() {
        for (String t : violations.keySet()) {
            int count = violations.get(t);
            Integer unstableLimit = config.getTypeConfigs().get(t).getUnstable();
            if (unstableLimit == null) {
                continue;
            }
            if (count >= unstableLimit) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the failed status for this report.
     *
     * @return true if one of the violations equals or exceed the failed
     *         threshold of that violations type.
     */
    private boolean isFailed() {
        for (String t : violations.keySet()) {
            int count = violations.get(t);
            Integer failCount = config.getTypeConfigs().get(t).getFail();
            if (failCount == null) {
                continue;
            }
            if (count >= failCount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set the unstable/failed status of a build based on this violations
     * report.
     */
    public void setBuildResult() {
        if (isFailed()) {
            build.setResult(Result.FAILURE);
            return;
        }
        if (isUnstable()) {
            try {
                build.setResult(Result.UNSTABLE);
            } catch (IllegalStateException e) {
                LOG.log(SEVERE,
                        "Cannot set build to unstable for maven jobs, only for freestyle jobs. Issue: JENKINS-25406", e);
            }
        }
    }

    private static final long serialVersionUID = 1L;

    public static ViolationsReport findViolationsReport(AbstractBuild<?, ?> b) {
        for (; b != null; b = b.getPreviousBuild()) {
            if (b.getResult().isWorseOrEqualTo(Result.FAILURE)) {
                continue;
            }
            AbstractViolationsBuildAction action = b.getAction(AbstractViolationsBuildAction.class);
            if (action == null || action.getReport() == null) {
                continue;
            }
            ViolationsReport ret = action.getReport();
            ret.setBuild(b);
            return ret;
        }
        return null;
    }

    public static ViolationsReportIterator iteration(AbstractBuild<?, ?> build) {
        return new ViolationsReportIterator(build);
    }

    public static class ViolationsReportIterator implements Iterator<ViolationsReport>, Iterable<ViolationsReport> {
        private AbstractBuild<?, ?> curr;

        public ViolationsReportIterator(AbstractBuild<?, ?> curr) {
            this.curr = curr;
        }

        @Override
        public Iterator<ViolationsReport> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return findViolationsReport(curr) != null;
        }

        @Override
        public ViolationsReport next() {
            ViolationsReport ret = findViolationsReport(curr);
            if (ret != null) {
                curr = ret.getBuild().getPreviousBuild();
            }
            return ret;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
