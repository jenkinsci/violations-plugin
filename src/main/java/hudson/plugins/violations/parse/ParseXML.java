package hudson.plugins.violations.parse;

import hudson.plugins.violations.util.CloseUtil;
import hudson.util.IOException2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Control class to parse xml files.
 */
public final class ParseXML {
    private static final Logger LOG
        = Logger.getLogger(ParseXML.class.getName());

    /** private constructor */
    private ParseXML() {
        // Does nothing
        LOG.log(Level.FINE, "private constructor called");
    }

    /**
     * Parse an Input stream using a parser object.
     * @param in the stream to parse.
     * @param xmlParser the parser object.
     * @throws IOException if there is a problem.
     */
    public static void parse(
        InputStream in, AbstractParser xmlParser)
        throws IOException {
        try {
            XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new XmlReader(in));
            xmlParser.setParser(parser);
            xmlParser.execute();
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new IOException2(ex);
        }
    }

    /**
     * Parse an xml file using a parser object.
     * @param xmlFile the file to parse.
     * @param xmlParser the parser object.
     * @throws IOException if there is a problem.
     */
    public static void parse(
        File         xmlFile,
        AbstractParser xmlParser) throws IOException {
        InputStream in = null;
        boolean     seenException = false;
        try {
            in = new FileInputStream(xmlFile);
            XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new XmlReader(in));
            xmlParser.setParser(parser);
            xmlParser.execute();

        } catch (IOException ex) {
            seenException = true;
            throw ex;
        } catch (Exception ex) {
            seenException = true;
            throw new IOException2(ex);
        } finally {
            CloseUtil.close(in, seenException);
        }
    }
}
