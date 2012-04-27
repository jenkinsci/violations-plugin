package hudson.plugins.violations;

import hudson.maven.AggregatableAction;
import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.AbstractBuild;
import hudson.plugins.violations.hudson.AbstractViolationsBuildAction;
import hudson.plugins.violations.hudson.maven.ViolationsMavenAggregatedBuildAction;

import java.util.List;
import java.util.Map;

/**
 * This is the build action for the
 * violations. It has the violation report for
 * the build and is able to graph the violations.
 * The rendering of the build is done by the associated
 * summary.jelly script.
 */

public class ViolationsBuildAction
    extends AbstractViolationsBuildAction
    implements AggregatableAction
{

    private static final double LOG_VALUE_FOR_ZERO = 0.5;
    private boolean  useLog = false;

    private static final int X_SIZE = 500;
    private static final int Y_SIZE = 200;
    private static final double PADDING = 5.0;

    private ViolationsReport report;

    /**
     * Construct a build action.
     * @param owner the build that has created this action.
     * @param report the report for this build.
     */
    public ViolationsBuildAction(
        AbstractBuild<?, ?> owner,
        ViolationsReport report) {
        super(owner);
        this.report = report;
    }

    /**
     * Constructor used for M2 projects.
     * This needs to be created during a "postExecute"
     * due to sequencing issue (1582). However,
     * it may be usefull to do this anyway.
     * report is set later.
     * @param owner the build that has created this action.
     */
    public ViolationsBuildAction(
        AbstractBuild<?, ?> owner) {
        super(owner);
    }

    public MavenAggregatedReport createAggregatedAction(
        MavenModuleSetBuild build,
        Map<MavenModule, List<MavenBuild>> moduleBuilds) {
        return new ViolationsMavenAggregatedBuildAction(build);
    }


    /**
     * Set the report.
     * Used in M2.
     * @param report the report for this build.
     */
    public void setReport(
        ViolationsReport report) {
        report.setBuild(getBuild());
        this.report = report;
    }

    public ViolationsReport getReport() {
        if (report != null) {
            // FIXME: really need to find the real reason for need to set build.
            report.setBuild(getBuild());
        }
        return report;
    }

    /**
     * get rhe previous valid build result.
     * @return the previous violations build action.
     */
    /*
    public ViolationsBuildAction getPreviousResult() {
        return (ViolationsBuildAction) super.getPreviousResult();
    }
    */

}
