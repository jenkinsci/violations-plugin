package  hudson.plugins.violations.hudson.maven;

import hudson.maven.MavenBuild;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.HealthReport;
import hudson.plugins.violations.ViolationsBuildAction;
import hudson.plugins.violations.ViolationsReport;
import hudson.plugins.violations.model.BuildModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViolationsAggregatedReport
    extends ViolationsReport {

    private List<ViolationsModuleReport> reports;
    private MavenModuleSetBuild mavenBuild;
    private HealthReport healthReport = null;

    public ViolationsAggregatedReport(MavenModuleSetBuild build) {
        setBuild(build);
        this.mavenBuild = build;
        this.reports = new ArrayList<ViolationsModuleReport>();
        init();
    }

    @Override
    public HealthReport getBuildHealth() {
        return healthReport;
    }

    @Override
    public BuildModel getModel() {
        return null;
    }

    public ViolationsReport getReportForMavenBuild(MavenBuild b) {
        for (ViolationsModuleReport moduleReport: reports) {
            if (moduleReport.getBuild().equals(b)) {
                return moduleReport.getReport();
            }
        }
        return null;
    }

    private void init() {
        for (MavenBuild b: mavenBuild.getModuleLastBuilds().values()) {
            ViolationsBuildAction a = b.getAction(ViolationsBuildAction.class);
            if (a == null || a.getReport() == null) {
                continue;
            }
            ViolationsModuleReport r = new ViolationsModuleReport(
                b, a.getReport());
            reports.add(r);
            HealthReport x = a.getReport().getBuildHealth();
            HealthReport aReport = null;
            if (x != null) {
                aReport = new HealthReport(
                    x.getScore(),
                    Messages._ViolationsAggregatedReport_HealthDescription(
                        x.getDescription(), r.getDisplayName()));
            }
            if (aReport != null) {
                healthReport = HealthReport.min(healthReport, aReport);
            }
            setConfig(a.getReport().getConfig());
            Map<String, Integer> aggregatedViolations = getViolations();
            for (Map.Entry<String, Integer> e:
                     a.getReport().getViolations().entrySet()) {
                int val = e.getValue();
                Integer current = aggregatedViolations.get(e.getKey());
                if (current == null) {
                    aggregatedViolations.put(e.getKey(), e.getValue());
                } else {
                    aggregatedViolations.put(e.getKey(), val + current);
                }
            }
        }
    }
}