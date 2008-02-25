package hudson.plugins.violations;

import java.io.File;
import java.io.IOException;

import java.util.Map;

import hudson.FilePath;
import hudson.Launcher;

import hudson.model.Action;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Project;
import hudson.tasks.Publisher;

import org.kohsuke.stapler.StaplerRequest;



/**
 * Generats HTML and XML reports from checkstyle, pmd and findbugs
 * report xml files.
 *
 * @author Peter Reilly
 */
public class ViolationsPublisher extends Publisher {

    private static final String VIOLATIONS = "violations";

    private final ViolationsConfig config =
        new  ViolationsConfig();

    /**
     * Get the confiation object for this violations publisher.
     * @return the config.
     */
    public ViolationsConfig getConfig() {
        return config;
    }

    /**
     * Get a copy of the configuration.
     * This is used to configure a new publisher in the config.jelly
     * script.
     * @return a copy o the condig.
     */
    public ViolationsConfig getOldConfig() {
        return config.clone();
    }

    /**
     * Called by hudson at the end of a buuld.
     * @param build the build
     * @param launcher the laucher
     * @param listener for reporting errors
     * @return true always.
     * @throws InterruptedException
     *             if user cancels the operation
     * @throws IOException
     *             if problem parsing the xml files
     */
    public boolean perform(
        Build<?, ?> build, Launcher launcher,
        BuildListener listener) throws InterruptedException, IOException {

        FilePath htmlPath   = new FilePath(
            new File(build.getProject().getRootDir(), VIOLATIONS));
        FilePath targetPath = new FilePath(
            new File(build.getRootDir(), VIOLATIONS));

        ViolationsReport report = build.getProject().getWorkspace().act(
            new ViolationsCollector(listener, targetPath, htmlPath, config));
        report.setConfig(config);
        report.setBuild(build);
        build.getActions().add(
            new ViolationsBuildAction(build, report));
        return true;
    }

    /**
     * Create a project action for a project.
     * @param project the project to create the action for.
     * @return the created violations project action.
     */
    public Action getProjectAction(Project project) {
        return new ViolationsProjectAction(project);
    }

    /**
     * Get the descriptor.
     * @return the violations publisher descriptor.
     */
    public Descriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

    /** The descriptor for this publisher - used in project config page. */
    public static final Descriptor<Publisher> DESCRIPTOR
        = new DescriptorImpl();

    /**
     * A class for  the plugin configuration screen in hudson.
     */
    public static final class DescriptorImpl extends Descriptor<Publisher> {

        /**
         * a private constructor.
         */
        private DescriptorImpl() {
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
        public Publisher newInstance(StaplerRequest req) {
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

    };

}
