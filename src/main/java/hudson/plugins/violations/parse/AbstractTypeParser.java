package hudson.plugins.violations.parse;

import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.util.CloseUtil;
import hudson.util.IOException2;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * An abstract xml parsing class for parsing
 * violation configuration files using an XML pull parser.
 */
public abstract class AbstractTypeParser
    extends AbstractParser implements ViolationsParser {
    private FullBuildModel model;
    private File           projectPath;
    private String[]       sourcePaths;

    /**
     * Parse a violations file.
     * @param model the model to store the violations in.
     * @param projectPath the project path used for resolving paths.
     * @param fileName the name of the violations file to parse
     *                       (relative to the projectPath).
     * @param sourcePaths a list of source paths to resolve classes against
     * @throws IOException if there is an error.
     */
    public void parse(
        FullBuildModel model,
        File           projectPath,
        String         fileName,
        String[]       sourcePaths)
        throws IOException {

        Reader in = null;
        boolean success = false;
        try {
            in = new XmlReader(new File(projectPath, fileName));
            XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(in);

            setProjectPath(projectPath);
            setModel(model);
            setParser(parser);
            setSourcePaths(sourcePaths);
            execute();
            success = true;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException2("Cannot parse " + fileName, ex);
        } finally {
            CloseUtil.close(in, !success);
        }
    }

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
        this.sourcePaths = sourcePaths.clone();
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
     * Fix an absolute path.
     * Some paths in some violations file contain a ..
     * this causes grief.
     * @param abs the absolute name.
     * @return a fixed name.
     * @throws IOException if there is a problem.
     */
    protected String fixAbsolutePath(String abs) throws IOException {
        if (abs.contains("..")) {
            return new File(abs).getCanonicalPath();
        } else {
            return abs;
        }
    }

    /**
     * Resolve an absolute name agaist the project path.
     * @param absoluteName the absolute name.
     * @return a path relative to the project path or an absolute
     *         name if the name cannot be resolved.
     */
    protected String resolveName(String absoluteName) {
        return ParseUtil.resolveAbsoluteName(projectPath, absoluteName);
    }
}
