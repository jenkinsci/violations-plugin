package hudson.plugins.violations;

import java.io.File;
import java.io.IOException;

import java.util.logging.Logger;

import org.apache.tools.ant.types.FileSet;

import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;

import hudson.plugins.violations.util.StringUtil;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.generate.GenerateXML;
import hudson.plugins.violations.parse.ParseTypeXML;

/**
 * Collects the violations xml files, analyses them, renders
 * html reports to the working directory, and returns a summary
 * Violations report.
 */

public class ViolationsCollector implements FileCallable<ViolationsReport> {
   private static final Logger LOG = Logger.getLogger(
        ViolationsCollector.class.getName());

    /** Logger for hudson. */
    private final transient BuildListener listener;

    private static final String[] NO_STRINGS = new String[]{};

    /** Working directory to copy xml files to. */
    private final FilePath targetDir;
    private final FilePath htmlDir;

    private final ViolationsConfig config;

    private FullBuildModel model;
    private File workspace;

    /**
     * Constructor.
     * @param listener the Logger
     * @param targetDir working directory to copy xml results to
     * @param htmlDir working directory to html reports to.
     * @param config  the violations configuration.
     */
    public ViolationsCollector(
        BuildListener listener, FilePath targetDir, FilePath htmlDir,
        ViolationsConfig config) {
        this.listener = listener;
        this.targetDir = targetDir;
        this.htmlDir = htmlDir;
        this.config = config;
    }

    private boolean empty(String str) {
        return (str == null) || str.equals("");
    }

    /**
     * Create a report.
     * @param workspace the current workspace.
     * @param channel   the virtual channel.
     * @return the report.
     * @throws IOException if there is a problem.
     */
    public ViolationsReport invoke(File workspace, VirtualChannel channel)
        throws IOException {
        this.workspace = workspace;

        // If the faux project path has been set, use that instead of
        // the given workspace
        if (!StringUtil.isBlank(config.getFauxProjectPath())) {
            this.workspace = new File(config.getFauxProjectPath());
            LOG.fine("Using faux workspace " + this.workspace);
        }

        // get the source path directories (if any)
        String[] sourcePaths = findAbsoluteDirs(
            workspace, config.getSourcePathPattern());

        for (String sp: sourcePaths) {
            LOG.fine("Using extra sourcePath " + sp);
        }

        // Create the report
        ViolationsReport report = new ViolationsReport();
        report.setConfig(config);

        // Build up the model
        this.model = new FullBuildModel();
        for (String type: config.getTypeConfigs().keySet()) {
            TypeConfig c = config.getTypeConfigs().get(type);
            TypeDescriptor typeDescriptor = TypeDescriptor.TYPES.get(type);
            if (empty(c.getPattern()) || typeDescriptor == null) {
                continue;
            }
            doType(c, typeDescriptor, sourcePaths, report);
        }
        model.cleanup();

        // -----
        // Write out the xml reports
        // ----
        try {
            new GenerateXML(targetDir , model, config).execute();
        } catch (InterruptedException ex) {
            throw new IOException(ex);
        }

        // -----
        // Create the summary report
        // ----
        for (String type: model.getTypeMap().keySet()) {
            report.getViolations().put(
                type, model.getCountNumber(type));
        }
        return report;
    }

    private void doType(
        TypeConfig c, TypeDescriptor t, String[] sourcePaths,
        ViolationsReport report)
        throws IOException {
        String[] xmlFiles = findFiles(workspace, c.getPattern());
        if (xmlFiles.length == 0) {
            report.getViolations().put(
                c.getType(), -1);
            return;
            /** FIXME - add to above!
            throw new RuntimeException(
                "No violation xml files of type " + c.getType()
                + " with pattern " + c.getPattern() + " were found!");
            **/
        }
        model.addType(c.getType());
        for (String xmlFile: xmlFiles) {
            new ParseTypeXML().parse(
                model, workspace, xmlFile, sourcePaths, t.getParser());
        }
    }

    //  TO DO : PLACE THESE IN A UTILITY CLASS
    /**
     * Returns an array with the filenames of the files
     * that match an Ant pattern using the workspace as the base
     * directory.
     *
     * @param workspaceRoot root directory of the workspace
     *
     * @return the filenames found.
     */
    private String[] findFiles(
        final File workspaceRoot, String pattern) {
        if (StringUtil.isBlank(pattern)) {
            return NO_STRINGS;
        }
        FileSet fileSet = new FileSet();
        org.apache.tools.ant.Project project
            = new org.apache.tools.ant.Project();
        fileSet.setProject(project);
        fileSet.setDir(workspaceRoot);
        fileSet.setIncludes(pattern);

        return fileSet.getDirectoryScanner(project).getIncludedFiles();
    }

    /**
     * Returns an array with the relative filenames of directories
     * that match an Ant pattern using the workspace as the base
     * directory.
     *
     * @param workspaceRoot root directory of the workspace
     *
     * @return the filenames found.
     */
    private String[] findDirs(
        final File workspaceRoot, String pattern) {
        if (StringUtil.isBlank(pattern)) {
            return NO_STRINGS;
        }
        FileSet fileSet = new FileSet();
        org.apache.tools.ant.Project project
            = new org.apache.tools.ant.Project();
        fileSet.setProject(project);
        fileSet.setDir(workspaceRoot);
        fileSet.setIncludes(pattern);

        return fileSet.getDirectoryScanner(project).getIncludedDirectories();
    }

    /**
     * Returns an array with the absolute filenames of directories
     * that match an Ant pattern using the workspace as the base
     * directory.
     *
     * @param workspaceRoot root directory of the workspace
     *
     * @return the filenames found.
     */
    private String[] findAbsoluteDirs(
        final File workspaceRoot, String pattern) {
        String[] relative = findDirs(workspaceRoot, pattern);
        if (relative.length == 0) {
            return relative;
        }
        String[] absolute = new String[relative.length];
        for (int i = 0; i < relative.length; ++i) {
            absolute[i] = new File(
                workspaceRoot, relative[i]).getAbsolutePath();
        }
        return absolute;
    }

    private static final long serialVersionUID = 1L;
}
