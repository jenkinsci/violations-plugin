package hudson.plugins.violations.hudson;

import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.StaplerProxy;

import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.AbstractProject;
import hudson.model.AbstractBuild;

import hudson.plugins.violations.MagicNames;

/**
 * Project level action.
 */
public abstract class AbstractViolationsProjectAction
    extends Actionable
    implements Action, StaplerProxy
    //,  ProminentProjectAction
{
    private final AbstractProject<?, ?> project;

    /**
     * Create a project action for the violations.
     * @param project the current project.
     */
    public AbstractViolationsProjectAction(AbstractProject<?, ?> project) {
        this.project = project;
    }

    /**
     * Get the project.
     * @return the project.
     */
    public AbstractProject<?, ?> getProject() {
        return project;
    }

    /**
     * Get the urlname for the proxy.
     * @return "violations"
     */
    public String getUrlName() {
        return MagicNames.VIOLATIONS;
    }

    /**
     * Get the search url for this.
     * @return "violations"
     */
    public String getSearchUrl() {
        return MagicNames.VIOLATIONS;
    }

    /**
     * Get the display name for the violations.
     * @return "Violations".
     */
    public String getDisplayName() {
        return "Violations";
    }
    /**
     * Get the target of the StaplerProxy for url violations.
     * @return the current violationsAction if one is present, null
     *         otherwise.
     */
    public Object getTarget() {
        return getViolationsAction();
    }


    /**
     * Get the icon file name.
     * @return the violations icon or null if not violations are present.
     */
    public String getIconFileName() {
        if (getViolationsAction() != null) {
            return MagicNames.ICON_24;
        } else {
            return null; // Hide as no violations available
        }
    }

    /**
     * Graph the violations.
     * This corresponds to violations/graph ?
     * @param req the StaplerRequest.
     * @param rsp the StaplerResponse.
     * @throws IOException if there is an problem writing the response.
     */
    public void doGraph(StaplerRequest req, StaplerResponse rsp)
        throws IOException {
        if (getViolationsAction() != null) {
            getViolationsAction().doGraph(req, rsp);
        }
    }

    /**
     * Get the violations action for this project.
     * This is defined as the most recent violations actions
     * of the builds of this project.
     * @return the most recent violations build action.
     */
    public AbstractViolationsBuildAction getViolationsAction() {

        for (AbstractBuild<?, ?> b = getProject().getLastBuild();
             b != null;
             b = b.getPreviousBuild()) {
            AbstractViolationsBuildAction ret = b.getAction(
                AbstractViolationsBuildAction.class);
            if (ret != null && ret.getReport() != null) {
                return ret;
            }
        }
        return null;
    }
}

