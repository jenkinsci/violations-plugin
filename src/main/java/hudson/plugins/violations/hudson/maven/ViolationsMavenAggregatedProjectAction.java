package hudson.plugins.violations.hudson.maven;


import hudson.model.AbstractProject;
import hudson.plugins.violations.MagicNames;
import hudson.plugins.violations.hudson.AbstractViolationsProjectAction;

public class ViolationsMavenAggregatedProjectAction
    extends AbstractViolationsProjectAction {

    public ViolationsMavenAggregatedProjectAction(
        AbstractProject<?, ?> project) {
        super(project);
    }

    public String getIconFileName() {
        return MagicNames.ICON_24;
    }
}
