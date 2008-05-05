package hudson.plugins.violations.hudson.maven;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hudson.model.Action;

import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;

import hudson.model.HealthReport;
import hudson.maven.AggregatableAction;

import hudson.plugins.violations.ViolationsBuildAction;
import hudson.plugins.violations.ViolationsReport;

import hudson.plugins.violations.hudson.*;

public class ViolationsMavenAggregatedBuildAction
    extends AbstractViolationsBuildAction
    implements MavenAggregatedReport {

    transient private ViolationsAggregatedReport aggregatedReport;

    public ViolationsMavenAggregatedBuildAction(
        MavenModuleSetBuild owner) {
        super(owner);
    }

    // --------------------
    // ViolationsAggregatedReport
    // --------------------

    public void update(
        Map<MavenModule,List<MavenBuild>> moduleBuilds, MavenBuild newBuild) {
    }

    public Class<? extends AggregatableAction> getIndividualActionType() {
        return ViolationsBuildAction.class;
    }

    public Action getProjectAction(MavenModuleSet moduleSet) {
        return new ViolationsMavenAggregatedProjectAction(moduleSet);
    }


    @Override
    public HealthReport getBuildHealth() {
        initReports();
        return super.getBuildHealth();
    }

    /**
     * Get the report.
     * @return the report.
     */
    public ViolationsAggregatedReport getReport() {
        initReports();
        aggregatedReport.setBuild(getBuild());
        return aggregatedReport;
    }

    /**
     * get rhe previous valid build result.
     * @return the previous violations build action.
     */
    public ViolationsMavenAggregatedBuildAction getPreviousResult() {
        return (ViolationsMavenAggregatedBuildAction) super.getPreviousResult();
    }

    private void initReports() {
        if (aggregatedReport != null) {
            return;
        }
        // FIXME: generics
        aggregatedReport = new ViolationsAggregatedReport(
            (MavenModuleSetBuild) getBuild());
    }

}
