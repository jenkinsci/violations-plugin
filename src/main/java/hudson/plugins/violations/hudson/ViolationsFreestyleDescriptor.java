package hudson.plugins.violations.hudson;

import java.util.Map;

import hudson.model.AbstractProject;
import hudson.tasks.Publisher;

import org.kohsuke.stapler.StaplerRequest;
import hudson.maven.AbstractMavenProject;
import hudson.tasks.BuildStepDescriptor;

import hudson.plugins.violations.ViolationsPublisher;
import hudson.plugins.violations.ViolationsConfig;
import hudson.plugins.violations.TypeConfig;
import net.sf.json.JSONObject;


/**
 * A class for  the plugin freestyle (and other?) configuration
 * screen in hudson.
 */
public final class ViolationsFreestyleDescriptor
    extends BuildStepDescriptor<Publisher> {

    /**
     * a constructor.
     */
    public ViolationsFreestyleDescriptor() {
        super(ViolationsPublisher.class);
        load();

    }

    /**
     * Get the name to display in the configuration screen for projects.
     * @return the name.
     */
    public String getDisplayName() {
        return "Report Violations";
    }

    /**
     * Get the help file for the configuration screen.
     * @return the url of the help file.
     */
    @Override
    public String getHelpFile() {
        return "/plugin/violations/help.html";
    }

    /**
     * Create a new instance of the ViolationsPublisher.
     * This gets configured from request parameters in 'req'.
     * @param req the reqest parameters from stapler.
     * @return a new ViolationsPublisher.
     */
    @Override
    public Publisher newInstance(StaplerRequest req, JSONObject formData) {
        ViolationsPublisher pub = new ViolationsPublisher();
        req.bindParameters(pub, "violations.");
        req.bindParameters(pub.getConfig(), "config.");
        for (Map.Entry<String, TypeConfig> entry
                 : pub.getConfig().getTypeConfigs().entrySet()) {
            String type = entry.getKey();
            TypeConfig c = entry.getValue();
            req.bindParameters(c, type + ".");
            // the ugly hack - see emma and clover plugins
            if ("".equals(req.getParameter(type + ".min"))) {
                c.setMin(TypeConfig.DEFAULT_MIN);
            }

            if ("".equals(req.getParameter(type + ".max"))) {
                c.setMax(TypeConfig.DEFAULT_MAX);
            }
        }
        // Ensure that the numbers make sense
        pub.getConfig().fix();
        return pub;
    }

    /**
     * Return a default config to be used for configuration screen.
     * @return a default config.
     */
    public ViolationsConfig getConfig() {
        return new ViolationsConfig();
    }

    /**
     * Return true that tis is free style descriptor.
     * @return true for this class.
     */
    public boolean isFreeStyle() {
        return true;
    }

    /**
     * Magic code to disable this publisher/descriptor appearing the
     * m2 project page.
     */
    @Override
    public boolean isApplicable(
        final Class<? extends AbstractProject> jobType) {
        return !AbstractMavenProject.class.isAssignableFrom(jobType);
    }


}
