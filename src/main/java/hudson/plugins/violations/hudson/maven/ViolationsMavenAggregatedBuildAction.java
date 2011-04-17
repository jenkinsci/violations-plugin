package hudson.plugins.violations.hudson.maven;

import hudson.maven.AggregatableAction;
import hudson.maven.MavenAggregatedReport;
import hudson.maven.MavenBuild;
import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.Action;
import hudson.plugins.violations.ViolationsBuildAction;
import hudson.plugins.violations.hudson.AbstractViolationsBuildAction;

import java.util.List;
import java.util.Map;

public class ViolationsMavenAggregatedBuildAction
    extends AbstractViolationsBuildAction
    implements MavenAggregatedReport {

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

    /**
     * Get the report.
     * @return the report.
     */
    @Override
    public ViolationsAggregatedReport getReport() {
        return new ViolationsAggregatedReport(
            (MavenModuleSetBuild) getBuild());
    }

    /**
     * get rhe previous valid build result.
     * @return the previous violations build action.
     */
    @Override
    public ViolationsMavenAggregatedBuildAction getPreviousResult() {
        return (ViolationsMavenAggregatedBuildAction) super.getPreviousResult();
    }

}
