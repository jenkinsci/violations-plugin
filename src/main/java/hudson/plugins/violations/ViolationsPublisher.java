package hudson.plugins.violations;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;
import hudson.plugins.violations.model.BuildModel;
import hudson.plugins.violations.render.FileModelProxy;
import hudson.tasks.BuildStepMonitor;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import hudson.FilePath;
import hudson.Launcher;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import hudson.plugins.violations.hudson.ViolationsFreestyleDescriptor;
import hudson.tasks.BuildStepDescriptor;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.database.PerItemDatabaseConfiguration;
import org.jenkinsci.plugins.database.jpa.PersistenceService;
import org.kohsuke.stapler.framework.io.IOException2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 * Generats HTML and XML reports from checkstyle, pmd and findbugs
 * report xml files.
 *
 * @author Peter Reilly
 */
public class ViolationsPublisher extends Recorder {

    private static final String VIOLATIONS = "violations";

    private final ViolationsConfig config =
        new  ViolationsConfig();

    /**
     * Get the configuration object for this violations publisher.
     * @return the config.
     */
    public ViolationsConfig getConfig() {
        return config;
    }

    /**
     * Get a copy of the configuration.
     * This is used to configure a new publisher in the config.jelly
     * script.
     * @return a copy o the config.
     */
    public ViolationsConfig getOldConfig() {
        return config.clone();
    }

    /**
     * Called by hudson at the end of a build.
     * @param build the build
     * @param launcher the launcher
     * @param listener for reporting errors
     * @return true always.
     * @throws InterruptedException
     *             if user cancels the operation
     * @throws IOException
     *             if problem parsing the xml files
     */
    @Override
    public boolean perform(
        AbstractBuild<?, ?> build, Launcher launcher,
        BuildListener listener) throws InterruptedException, IOException {

        FilePath htmlPath   = new FilePath(
            new File(build.getProject().getRootDir(), VIOLATIONS));
        FilePath targetPath = new FilePath(
            new File(build.getRootDir(), VIOLATIONS));

        ViolationsReport report = build.getWorkspace().act(
            new ViolationsCollector(false, targetPath, htmlPath, config));
        report.setConfig(config);
        report.setBuild(build);
        report.setBuildResult();
        build.getActions().add(
            new ViolationsBuildAction(build, report));

        try {
            PersistenceService ps = Jenkins.getInstance().getInjector().getInstance(PersistenceService.class);
            EntityManagerFactory emf = ps.getPerItemEntityManagerFactory((TopLevelItem)build.getProject());// FIXME
            try {
                EntityManager em = emf.createEntityManager();
                try {
                    BuildModel bm = report.getModel();
                    for (FileModelProxy fmp : bm.getFileModelMap().values()) {
                        em.persist(fmp.getFileModel());
                    }
                } finally {
                    em.close();
                }
            } finally {
                emf.close();
            }
        } catch (SQLException e) {
            throw new IOException2(e);
        }

        return true;
    }

    /**
     * Create a project action for a project.
     * @param project the project to create the action for.
     * @return the created violations project action.
     */
    @Override
    public Action getProjectAction(AbstractProject<?, ?> project) {
        return new ViolationsProjectAction(project);
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    /**
     * Get the descriptor.
     * @return the violations publisher descriptor.
     */
    @Override
    public BuildStepDescriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }

    /** The descriptor for this publisher - used in project config page. */
    @Extension
    public static final BuildStepDescriptor<Publisher> DESCRIPTOR
        = new ViolationsFreestyleDescriptor();

}
