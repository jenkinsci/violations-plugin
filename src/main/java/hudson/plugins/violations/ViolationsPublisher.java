package hudson.plugins.violations;

import static hudson.model.Result.SUCCESS;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.Result;
import hudson.model.AbstractProject;
import hudson.plugins.violations.ViolationsReport.TypeReport;
import hudson.plugins.violations.hudson.ViolationsFreestyleDescriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.google.common.annotations.VisibleForTesting;
import hudson.model.Run;
import hudson.model.TaskListener;
import javax.annotation.Nonnull;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Generats HTML and XML reports from checkstyle, pmd and findbugs report xml
 * files.
 *
 * @author Peter Reilly
 */
public class ViolationsPublisher extends Recorder implements SimpleBuildStep {

    private static final String VIOLATIONS = "violations";

    protected final ViolationsConfig config = new ViolationsConfig();
    
    public ViolationsPublisher(){
    }
    
    @DataBoundConstructor
    public ViolationsPublisher(String type, int sunny, int stormy, int unstable, String pattern, boolean autoUpdateStormyThreshold, boolean autoUpdateUnstableThreshold){
        TypeConfig tc = new TypeConfig(type);
        tc.setMin(sunny);
        tc.setMax(stormy);
        tc.setUnstable(unstable);
        tc.setPattern(pattern);
        this.config.getTypeConfigs().put(type, tc);
        this.config.setAutoUpdateMax(autoUpdateStormyThreshold);
        this.config.setAutoUpdateUnstable(autoUpdateUnstableThreshold);
    }

    /**
     * Get the configuration object for this violations publisher.
     *
     * @return the config.
     */
    public ViolationsConfig getConfig() {
        return config;
    }

    /**
     * Get a copy of the configuration. This is used to configure a new
     * publisher in the config.jelly script.
     *
     * @return a copy o the config.
     */
    public ViolationsConfig getOldConfig() {
        return config.clone();
    }

    /**
     * Called by hudson at the end of a build.
     *
     * @param build
     *            the build
     * @param workspace
     * @param launcher
     *            the launcher
     * @param listener
     *            for reporting errors
     * @return true always.
     * @throws InterruptedException
     *             if user cancels the operation
     * @throws IOException
     *             if problem parsing the xml files
     */
    @Override
    public void perform(Run<?, ?> build, @Nonnull FilePath workspace, Launcher launcher, TaskListener listener) throws IOException, InterruptedException {
        FilePath htmlPath = new FilePath(new File(build.getRootDir(), VIOLATIONS));
        FilePath targetPath = new FilePath(new File(build.getRootDir(), VIOLATIONS));

        build.getActions().add(createBuildAction(workspace, targetPath, htmlPath, config, build, listener));
    }

    @VisibleForTesting
    static ViolationsBuildAction createBuildAction(FilePath workspace, FilePath targetPath, FilePath htmlPath,
            ViolationsConfig config, Run<?, ?> build, TaskListener listener) throws IOException,
            InterruptedException {
        ViolationsReport report = workspace.act(new ViolationsCollector(false, targetPath, htmlPath, config));
        report.setConfig(config);
        report.setBuild(build);
        report.setBuildResult();
        handleRatcheting(report.getBuild().getResult(), report.getTypeReports().values(), listener, config);
        return new ViolationsBuildAction(build, report);
    }

    /**
     * Perform ratcheting if enabled, i.e. lower the thresholds if the build is
     * stable and the current value is lower than the current threshold.
     */
    static void handleRatcheting(Result result, Collection<TypeReport> typeReports, TaskListener listener,
            ViolationsConfig config) {
        if (!shouldDoRatcheting(config, result)) {
            return;
        }

        // adjust the single configs (if needed)
        for (TypeReport typeReport : typeReports) {
            TypeConfig typeConfig = config.getTypeConfigs().get(typeReport.getType());
            int thresholdCount = typeReport.getNumber() + 1;

            if (config.isAutoUpdateUnstable() && thresholdCount < typeConfig.getUnstable()) {
                listener.getLogger().println(
                        "Setting unstable value for " + typeConfig.getType() + " to " + thresholdCount);
                typeConfig.setUnstable(thresholdCount);
            }

            if (config.isAutoUpdateMax() && thresholdCount < typeConfig.getMax()) {
                listener.getLogger().println(
                        "Setting max/stormy value for " + typeConfig.getType() + " to " + thresholdCount);
                typeConfig.setMax(thresholdCount);
                // fix the min value but don't use fix() because it changes max
                // (and not min)
                if (typeConfig.getMin() >= typeConfig.getMax()) {
                    typeConfig.setMin(typeConfig.getMax() - 1);
                }
            }
        }
    }

    static boolean shouldDoRatcheting(ViolationsConfig config, Result result) {
        if (!config.isAutoUpdateMax() && !config.isAutoUpdateUnstable()) {
            return FALSE;
        }

        if (result != SUCCESS) {
            return FALSE;
        }

        return TRUE;
    }

    /**
     * Create a project action for a project.
     *
     * @param project
     *            the project to create the action for.
     * @return the created violations project action.
     */
    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new ViolationsProjectAction(project);
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    /**
     * Get the descriptor.
     *
     * @return the violations publisher descriptor.
     */
    @Override
    public BuildStepDescriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

    /** The descriptor for this publisher - used in project config page. */
    @Extension
    public static final BuildStepDescriptor<Publisher> DESCRIPTOR = new ViolationsFreestyleDescriptor();

}
