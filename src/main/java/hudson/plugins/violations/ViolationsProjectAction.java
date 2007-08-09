package hudson.plugins.violations;

import java.io.IOException;

import java.text.NumberFormat;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.StaplerProxy;

import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.Project;

/**
 * Project level action.
 */
public class ViolationsProjectAction
    extends Actionable implements Action, StaplerProxy {
    private final Project<?, ?> project;
    private static final NumberFormat NUMBER_FORMAT
        = NumberFormat.getInstance();

    /**
     * Create a project action for the violations.
     * @param project the current project.
     */
    public  ViolationsProjectAction(Project project) {
        this.project = project;
    }

    /*
    public static File getViolationsWorkspace(AbstractItem p) {
        return new File(p.getRootDir(), "violations");
    }
    */

    /**
     * Get the target of the StaplerProxy for url violations.
     * @return the current violationsAction if one is present, null
     *         otherwise.
     */
    public Object getTarget() {
        if (getViolationsAction() == null) {
            return null;
        }
        return getViolationsAction();
    }


    /**
     * Get the urlname for the proxy.
     * @return "violations" if a violations action is avail.
     */
    public String getUrlName() {
        if (getViolationsAction() == null) {
            return null;
        }
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
        System.out.println("ViolationsProjectActiion:doGraph()");
        if (getViolationsAction() != null) {
            getViolationsAction().doGraph(req, rsp);
        }
    }

    /**
     * Get the last violations action.
     * @return the last violations action, or null if not present.
     */
    public ViolationsBuildAction getViolationsAction() {
        return ViolationsBuildAction.getViolationsAction(
            project.getLastBuild());
    }
}

