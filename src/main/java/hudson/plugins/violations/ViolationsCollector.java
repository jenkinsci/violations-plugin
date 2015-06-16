package hudson.plugins.violations;

import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.plugins.violations.generate.GenerateXML;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.util.StringUtil;
import hudson.remoting.VirtualChannel;
import hudson.util.IOException2;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.tools.ant.types.FileSet;
import org.jenkinsci.remoting.RoleChecker;

/**
 * Collects the violations xml files, analyses them, renders html reports to the
 * working directory, and returns a summary Violations report.
 */

public class ViolationsCollector implements FileCallable<ViolationsReport> {
    private static final Logger LOG = Logger.getLogger(ViolationsCollector.class.getName());

    private static final String[] NO_STRINGS = new String[] {};

    private final boolean mavenProject;
    /** Working directory to copy xml files to. */
    private final FilePath targetDir;
    private final ViolationsConfig config;

    private FullBuildModel model;
    private File workspace;

    /**
     * Constructor.
     * 
     * @param mavenProject
     *            true if this a maven project, false otherwise
     * @param targetDir
     *            working directory to copy xml results to
     * @param htmlDir
     *            working directory to html reports to.
     * @param config
     *            the violations configuration.
     */
    public ViolationsCollector(boolean mavenProject, FilePath targetDir, FilePath htmlDir, ViolationsConfig config) {
        this.mavenProject = mavenProject;
        this.targetDir = targetDir;
        this.config = config;
    }

    private boolean empty(String str) {
        return (str == null) || str.equals("");
    }

    /**
     * Create a report.
     * 
     * @param workspace
     *            the current workspace.
     * @param channel
     *            the virtual channel.
     * @return the report.
     * @throws IOException
     *             if there is a problem.
     */
    @Override
    public ViolationsReport invoke(File workspace, VirtualChannel channel) throws IOException {
        this.workspace = workspace;

        // If the faux project path has been set, use that instead of
        // the given workspace
        if (!StringUtil.isBlank(config.getFauxProjectPath())) {
            this.workspace = new File(config.getFauxProjectPath());
            LOG.fine("Using faux workspace " + this.workspace);
        }

        String[] sourcePaths = null;

        if (mavenProject) {
            sourcePaths = new String[] { workspace.toString() + "/src/main/java" };
        } else {
            // get the source path directories (if any)
            sourcePaths = findAbsoluteDirs(workspace, config.getSourcePathPattern());
        }

        for (String sp : sourcePaths) {
            LOG.fine("Using extra sourcePath " + sp);
        }

        // Create the report
        ViolationsReport report = new ViolationsReport();
        report.setConfig(config);

        // Build up the model
        this.model = new FullBuildModel();
        for (String type : config.getTypeConfigs().keySet()) {
            TypeConfig c = config.getTypeConfigs().get(type);
            TypeDescriptor typeDescriptor = TypeDescriptor.TYPES.get(type);
            if (typeDescriptor == null) {
                continue;
            }
            if (mavenProject && (typeDescriptor.getMavenTargets() != null)) {
                doType(c, typeDescriptor, sourcePaths, report);
                continue;
            }
            if (empty(c.getPattern())) {
                continue;
            }
            doType(c, typeDescriptor, sourcePaths, report);
        }
        model.cleanup();

        // -----
        // Write out the xml reports
        // ----
        try {
            new GenerateXML(targetDir, model, config).execute();
        } catch (InterruptedException ex) {
            throw new IOException2(ex);
        }

        // -----
        // Create the summary report
        // ----
        for (String type : model.getTypeMap().keySet()) {
            report.getViolations().put(type, model.getCountNumber(type));
            doSeverities(report, type);
        }
        return report;
    }

    /**
     * Get all the severities of a particular type and update the severity array
     * of the type summary.
     */
    private void doSeverities(ViolationsReport report, String type) {
        TypeSummary summary = report.getTypeSummary(type);
        for (FullFileModel file : model.getFileModelMap().values()) {
            if (file.getTypeMap().get(type) == null) {
                continue;
            }
            for (Violation v : file.getTypeMap().get(type)) {
                summary.getSeverityArray()[v.getSeverityLevel()]++;
            }
        }
    }

    // TODO: to many if, else, break and return
    private void doType(TypeConfig c, TypeDescriptor t, String[] sourcePaths, ViolationsReport report)
            throws IOException {
        String[] fileNames = null;
        if (mavenProject && t.getMavenTargets() != null && !c.isUsePattern()) {
            for (String name : t.getMavenTargets()) {
                fileNames = findFiles(workspace, "target/" + name);
                if (fileNames.length != 0) {
                    break;
                }
            }
            if (fileNames.length == 0) {
                return;
            }
        } else {
            fileNames = findFiles(workspace, c.getPattern());
            if (fileNames.length == 0) {
                if (!mavenProject) {
                    report.getViolations().put(c.getType(), -1);
                    report.getTypeSummary(c.getType()).setErrorMessage(
                            "No violation report files of type " + c.getType() + " with pattern " + c.getPattern()
                                    + " were found!");
                }
                return;
            }
        }
        model.addType(c.getType());
        for (String fileName : fileNames) {
            t.createParser().parse(model, workspace, fileName, sourcePaths);
        }
    }

    // TO DO : PLACE THESE IN A UTILITY CLASS
    /**
     * Returns an array with the filenames of the files that match an Ant
     * pattern using the workspace as the base directory.
     *
     * @param workspaceRoot
     *            root directory of the workspace
     *
     * @return the filenames found.
     */
    private String[] findFiles(final File workspaceRoot, String pattern) {
        if (StringUtil.isBlank(pattern)) {
            return NO_STRINGS;
        }
        FileSet fileSet = new FileSet();
        org.apache.tools.ant.Project project = new org.apache.tools.ant.Project();
        fileSet.setProject(project);
        fileSet.setDir(workspaceRoot);
        fileSet.setIncludes(pattern);

        return fileSet.getDirectoryScanner(project).getIncludedFiles();
    }

    /**
     * Returns an array with the relative filenames of directories that match an
     * Ant pattern using the workspace as the base directory.
     *
     * @param workspaceRoot
     *            root directory of the workspace
     *
     * @return the filenames found.
     */
    private String[] findDirs(final File workspaceRoot, String pattern) {
        if (StringUtil.isBlank(pattern)) {
            return NO_STRINGS;
        }
        FileSet fileSet = new FileSet();
        org.apache.tools.ant.Project project = new org.apache.tools.ant.Project();
        fileSet.setProject(project);
        fileSet.setDir(workspaceRoot);
        fileSet.setIncludes(pattern);

        return fileSet.getDirectoryScanner(project).getIncludedDirectories();
    }

    /**
     * Returns an array with the absolute filenames of directories that match an
     * Ant pattern using the workspace as the base directory.
     *
     * @param workspaceRoot
     *            root directory of the workspace
     *
     * @return the filenames found.
     */
    private String[] findAbsoluteDirs(final File workspaceRoot, String pattern) {
        String[] relative = findDirs(workspaceRoot, pattern);
        if (relative.length == 0) {
            return relative;
        }
        String[] absolute = new String[relative.length];
        for (int i = 0; i < relative.length; ++i) {
            absolute[i] = new File(workspaceRoot, relative[i]).getAbsolutePath();
        }
        return absolute;
    }

    private static final long serialVersionUID = 1L;

    // Needed to build with Jenkins 1.609, dont @Override since it will cause
    // errors when building for older Jenkins
    public void checkRoles(RoleChecker checker) throws SecurityException {
    }
}
