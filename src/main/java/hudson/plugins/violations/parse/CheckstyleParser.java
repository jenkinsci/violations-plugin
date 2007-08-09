package hudson.plugins.violations.parse;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;


import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.model.FullFileModel;

/**
 * Parses a checkstyle xml report file.
 */
public class CheckstyleParser extends AbstractTypeParser {

    /**
     * Parse the checkstyle xml file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute()
        throws IOException, XmlPullParserException {

        // Ensure that the top level tag is "checkstyle"
        expectNextTag("checkstyle");
        getParser().next(); // consume the "checkstyle" tag
        // loop tru the child elements, getting the "file" ones
        while (skipToTag("file")) {
            parseFileElement();
        }
    }

    private void parseFileElement()
        throws IOException, XmlPullParserException {

        String absoluteFileName = checkNotBlank("name");
        getParser().next();  // consume "file" tag
        FullFileModel fileModel = getFileModel(absoluteFileName);

        // Loop tru the child elements, getting the "error" ones
        while (skipToTag("error")) {
            fileModel.addViolation(parseErrorElement());
        }
        endElement();
    }

    private Violation parseErrorElement()
        throws IOException, XmlPullParserException {
        Violation ret = new Violation();
        ret.setType("checkstyle");
        ret.setLine(getParser().getAttributeValue("", "line"));
        ret.setMessage(getParser().getAttributeValue("", "message"));
        ret.setSource(getParser().getAttributeValue("", "source"));
        ret.setSeverity(getParser().getAttributeValue("", "severity"));
        getParser().next();
        endElement();
        return ret;
    }
}
