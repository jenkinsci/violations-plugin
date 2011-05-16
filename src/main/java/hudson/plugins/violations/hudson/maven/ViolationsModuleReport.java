package hudson.plugins.violations.hudson.maven;

import hudson.maven.MavenBuild;
import hudson.plugins.violations.ViolationsReport;

/**
 * A report from a particular module.
 */
public class ViolationsModuleReport {
    private final MavenBuild mavenBuild;
    private final ViolationsReport report;

    public ViolationsModuleReport(
        MavenBuild mavenBuild,
        ViolationsReport report) {
        this.mavenBuild = mavenBuild;
        this.report = report;
    }

    public String getDisplayName() {
        return mavenBuild.getProject().getDisplayName();
    }

    public MavenBuild getBuild() {
        return mavenBuild;
    }

    public ViolationsReport getReport() {
        return report;
    }
}
