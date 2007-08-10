package hudson.plugins.violations.parse;

import java.io.File;

import java.util.Locale;

import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;

/**
 * An abstract xml parsing class for parsing
 * violation configuration files.
 */
public abstract class AbstractTypeParser
    extends AbstractParser {
    private FullBuildModel model;
    private File           projectPath;
    private String[]       sourcePaths;

    /**
     * Set the build model.
     * @param model the model.
     */
    public void setModel(FullBuildModel model) {
        this.model = model;
    }

    /**
     * Get the build model.
     * @return the build model.
     */
    protected FullBuildModel getModel() {
        return model;
    }

    /**
     * Set the project path.
     * This is used to get a relative name for files.
     * @param projectPath the project path.
     */
    public void setProjectPath(File projectPath) {
        this.projectPath = projectPath;
    }

    /**
     * Get the project path.
     * @return the project path.
     */
    protected File getProjectPath() {
        return projectPath;
    }

    /**
     * Set the source paths attribute.
     * This is used to resolve classes against.
     * @param sourcePaths the value to use
     */
    public void setSourcePaths(String[] sourcePaths) {
        this.sourcePaths = sourcePaths;
    }

    /**
     * Get the source paths.
     * @return the source paths.
     */
    protected String[] getSourcePaths() {
        return sourcePaths;
    }

    // -----------------------------------------------
    //
    //   Utility methods
    //
    // -----------------------------------------------

    /**
     * Get the full file model for a particular relative name and
     * source file.
     * @param name the relative file name.
     * @param sourceFile the source file for the file.
     * @return the full file model.
     */
    protected FullFileModel getFileModel(String name, File sourceFile) {
        FullFileModel fileModel = model.getFileModel(name);
        File other = fileModel.getSourceFile();

        if (sourceFile == null
            || ((other != null) && (
                    other.equals(sourceFile)
                    || other.exists()))) {
            return fileModel;
        }
        fileModel.setSourceFile(sourceFile);
        fileModel.setLastModified(sourceFile.lastModified());
        return fileModel;
    }

    /**
     * Get the full file model object for an absolute name.
     * @param absoluteName the absolute name of the file.
     * @return the file model object.
     */
    protected FullFileModel getFileModel(String absoluteName) {
        File file = new File(absoluteName);
        String name = resolveName(absoluteName);
        return getFileModel(name, file);
    }

    /**
     * Resolve an absolute name agaist the project path.
     * @param absoluteName the absolute name.
     * @return a path relative to the project path or an absolute
     *         name if the name cannot be resolved.
     */
    protected String resolveName(String absoluteName) {
        String remote = projectPath.getAbsolutePath().replace('\\', '/');
        String lcRemote = remote.toLowerCase(Locale.US);
        String name = absoluteName.replace('\\', '/');
        String lcName = name.toLowerCase(Locale.US);
        if (lcName.startsWith(lcRemote)) {
            name = name.substring(lcRemote.length());
        } else {
            // for absolute name discard dos drive
            int colPos = name.indexOf(':');
            int dirPos = name.indexOf('/');
            if (colPos != -1 && (dirPos == -1 || dirPos > colPos)) {
                name = name.substring(colPos);
            }
        }
        // if name starts with a / strip it.
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;
    }
}
