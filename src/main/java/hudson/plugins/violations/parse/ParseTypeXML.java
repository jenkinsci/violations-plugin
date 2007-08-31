package hudson.plugins.violations.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;

import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.util.CloseUtil;

/**
 * Parse a violation type xml file.
 */
public class ParseTypeXML {

    private static final Logger LOG = Logger.getLogger(
        ParseTypeXML.class.getName());

    /**
     * Parse a xml violation file.
     * @param model the model to store the violations in.
     * @param projectPath the project path used for resolving paths.
     * @param xmlFile the xml file to parse.
     * @param typeParser the parser to use.
     * @param sourcePaths a list of source paths to resolve classes against
     * @throws IOException if there is an error.
     */
    public void parse(
        FullBuildModel model,
        File           projectPath,
        String         xmlFile,
        String[]       sourcePaths,
        AbstractTypeParser typeParser) throws IOException {
        LOG.info("Parsing " + xmlFile);
        InputStream in = null;
        boolean success = false;
        try {
            in = projectPath == null
                ? new FileInputStream(new File(xmlFile))
                : new FileInputStream(new File(projectPath, xmlFile));
            XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(in, null);

            typeParser.setProjectPath(projectPath);
            typeParser.setModel(model);
            typeParser.setParser(parser);
            typeParser.setSourcePaths(sourcePaths);
            typeParser.execute();
            success = true;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException(ex);
        } finally {
            CloseUtil.close(in, !success);
        }
    }
}
