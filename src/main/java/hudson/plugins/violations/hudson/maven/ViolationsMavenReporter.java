package hudson.plugins.violations.hudson.maven;

import hudson.Extension;
import hudson.maven.MavenBuild;
import hudson.maven.MavenReporter;
import hudson.maven.MavenReporterDescriptor;
import hudson.maven.MavenModule;
import hudson.maven.MavenBuildProxy;
import hudson.maven.MojoInfo;

import java.io.File;
import java.io.IOException;

import hudson.maven.MavenBuildProxy.BuildCallable;

import hudson.plugins.violations.ViolationsConfig;
import hudson.plugins.violations.ViolationsProjectAction;
import hudson.plugins.violations.ViolationsBuildAction;
import hudson.plugins.violations.ViolationsReport;
import hudson.plugins.violations.ViolationsCollector;
import hudson.model.BuildListener;
import hudson.Launcher;
import org.apache.maven.project.MavenProject;
import hudson.model.Action;
import hudson.FilePath;
import hudson.maven.MavenModuleSetBuild;

public class ViolationsMavenReporter extends MavenReporter {
    private static final String VIOLATIONS = "violations";

    @Extension
    public static final ViolationsMavenDescriptor DESCRIPTOR
        = new ViolationsMavenDescriptor();

    private final ViolationsConfig config =
        new  ViolationsConfig();

    private transient boolean registered;

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
     * Called at the end of each maven ?goal?.
     * - so if the maven args are pmd:pmd checkstyle:checkstyle
     * this will get called twice.
     */
    @Override
    public boolean postExecute(
        MavenBuildProxy build, MavenProject pom, MojoInfo mojo,
        BuildListener listener, Throwable error)
        throws InterruptedException, IOException {
        build.execute(new BuildCallable<Void, IOException>() {
            public Void call(final MavenBuild build)
                throws IOException, InterruptedException {
                // Create the violations build action - if not already built.
                // This needes to be done here, otherwise aggregated actions
                // do not get created.
                // (aggreatedactions get created after the postExecute(), but
                // before the end()
                getCreateBuildAction(build);
                return null;
            }
        });
        // Need to register the  MavenReporter as a ProjectAction
        if (!registered) {
            build.registerAsProjectAction(this);
            registered = true;
        }
        return true;
    }

    public boolean end(MavenBuild build, Launcher launcher, BuildListener listener)
        throws InterruptedException, IOException {
        registered = false;
        FilePath htmlPath   = new FilePath(
            new File(build.getProject().getRootDir(), VIOLATIONS));
        FilePath targetPath = new FilePath(
            new File(build.getRootDir(), VIOLATIONS));

        FilePath workspace = build.getWorkspace();
        if (workspace == null) {
            MavenModuleSetBuild parent = build.getModuleSetBuild();
            throw new IOException("No workspace for " + build + "; parent workspace: " + (parent != null ? parent.getWorkspace() : "N/A") + "; builtOnStr=" + build.getBuiltOnStr() + "; builtOn=" + build.getBuiltOn());
        }
        ViolationsReport report = workspace.act(
            new ViolationsCollector(true, targetPath, htmlPath, config));
        report.setConfig(config);
        report.setBuild(build);
        report.setBuildResult();
        
        ViolationsBuildAction buildAction = getCreateBuildAction(build);
        buildAction.setReport(report);
        return true;
    }

    @Override
    public Action getProjectAction(final MavenModule module) {
        return new ViolationsProjectAction(module);
    }

    /** {@inheritDoc} */
    @Override
    public MavenReporterDescriptor getDescriptor() {
        return DESCRIPTOR;
    }

    private ViolationsBuildAction getCreateBuildAction(MavenBuild build) {
        ViolationsBuildAction ret
            = build.getAction(ViolationsBuildAction.class);
        if (ret == null) {
            ret = new ViolationsBuildAction(build);
            build.getActions().add(ret);
        }
        return ret;
    }
    
    private static final long serialVersionUID = 1L;
}
