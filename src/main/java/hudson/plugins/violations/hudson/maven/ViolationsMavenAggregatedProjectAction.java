package hudson.plugins.violations.hudson.maven;


import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Actionable;
import hudson.maven.MavenModuleSetBuild;
import hudson.maven.MavenModuleSet;

import hudson.plugins.violations.MagicNames;

import hudson.plugins.violations.hudson.*;

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
