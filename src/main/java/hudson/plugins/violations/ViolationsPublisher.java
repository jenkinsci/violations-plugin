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
import hudson.model.AbstractProject;
import hudson.tasks.Publisher;

import org.kohsuke.stapler.StaplerRequest;
import hudson.maven.AbstractMavenProject;
import hudson.tasks.BuildStepDescriptor;

import hudson.plugins.violations.hudson.ViolationsFreestyleDescriptor;


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
    public Action getProjectAction(AbstractProject<?, ?> project) {
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
        = new ViolationsFreestyleDescriptor();

}
