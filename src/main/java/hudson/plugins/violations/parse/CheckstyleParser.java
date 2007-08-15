package hudson.plugins.violations.parse;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;


import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;

import hudson.plugins.violations.util.HashMapWithDefault;

/**
 * Parses a checkstyle xml report file.
 */
public class CheckstyleParser extends AbstractTypeParser {

    private static final HashMapWithDefault<String, String> SEVERITIES
        = new HashMapWithDefault<String, String>(Severity.MEDIUM);

    static {
        SEVERITIES.put("error", Severity.MEDIUM_HIGH);
        SEVERITIES.put("warning", Severity.MEDIUM);
        SEVERITIES.put("info", Severity.LOW);
    }


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
        ret.setLine(getString("line"));
        ret.setMessage(getString("message"));
        ret.setSource(getString("source"));
        setSeverity(ret, getString("severity"));
        getParser().next();
        endElement();
        return ret;
    }

    private void setSeverity(Violation v, String severity) {
        v.setSeverity(SEVERITIES.get(severity));
        v.setSeverityLevel(Severity.getSeverityLevel(
                               v.getSeverity()));
    }
}
