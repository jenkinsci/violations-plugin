package hudson.plugins.violations;

import hudson.FilePath;
import hudson.FilePath.FileCallable;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.types.FileSet;

import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.generate.GenerateXML;
import hudson.plugins.violations.parse.ParseTypeXML;

/**
 * Collects the violations xml files, analyses them, renders
 * html reports to the working directory, and returns a summary
 * Violations report.
 */

public class ViolationsCollector implements FileCallable<ViolationsReport> {

    /** Logger. */
    private final transient BuildListener listener;

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
     * @param channel   the virutal channel.
     * @return the report.
     * @throws IOException if there is a problem.
     */
    public ViolationsReport invoke(File workspace, VirtualChannel channel)
        throws IOException {
        this.workspace = workspace;

        // Create the report
        ViolationsReport report = new ViolationsReport();
        report.setConfig(config);

        // Build up the model
        this.model = new FullBuildModel();
        for (String type: config.getTypeConfigs().keySet()) {
            TypeConfig c = config.getTypeConfigs().get(type);
            TypeDescriptor t = TypeDescriptor.TYPES.get(type);
            if (empty(c.getPattern()) || t == null) {
                continue;
            }
            doType(c, t, report);
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

    private void doType(TypeConfig c, TypeDescriptor t, ViolationsReport report)
        throws IOException {
        String[] xmlFiles = findViolationsFiles(workspace, c.getPattern());
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
                model, workspace, xmlFile, t.getParser());
        }
    }

    /**
     * Returns an array with the filenames of the violations files that have
     * been found in the workspace.
     *
     * @param workspaceRoot root directory of the workspace
     *
     * @return the filenames of the violations xml files
     */
    private String[] findViolationsFiles(
        final File workspaceRoot, String pattern) {
        FileSet fileSet = new FileSet();
        org.apache.tools.ant.Project project
            = new org.apache.tools.ant.Project();
        fileSet.setProject(project);
        fileSet.setDir(workspaceRoot);
        fileSet.setIncludes(pattern);

        return fileSet.getDirectoryScanner(project).getIncludedFiles();
    }
}
