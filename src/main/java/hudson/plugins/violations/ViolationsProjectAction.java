package hudson.plugins.violations;

import java.io.IOException;

import java.text.NumberFormat;

import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.AbstractProject;

import hudson.plugins.violations.hudson.AbstractViolationsProjectAction;

/**
 * Project level action.
 */
public class ViolationsProjectAction
    extends AbstractViolationsProjectAction {

    /**
     * Create a project action for the violations.
     * @param project the current project.
     */
    public  ViolationsProjectAction(AbstractProject<?, ?> project) {
        super(project);
    }

}

