package hudson.plugins.violations.types.csslint;

import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.AbstractTypeParser;
import hudson.plugins.violations.util.HashMapWithDefault;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class CssLintParser extends AbstractTypeParser {

    static final String TYPE_NAME = "csslint";

    private static final HashMapWithDefault<String, String> SEVERITIES
        = new HashMapWithDefault<String, String>(Severity.HIGH);

    static {
        SEVERITIES.put("error", Severity.HIGH);
        SEVERITIES.put("warning", Severity.MEDIUM);
        SEVERITIES.put("info", Severity.LOW);
    }

    /**
     * Parse the CSSLint xml file.
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute() throws IOException, XmlPullParserException {
        
        // ensure that the top level tag is "lint"
        expectNextTag("lint");
        getParser().next(); // consume the "lint" tag
        
        // loop thru the child elements, getting the "file" ones
        while (skipToTag("file")) {
            parseFileElement();
        }
    }

    private void parseFileElement()
        throws IOException, XmlPullParserException {

        String absoluteFileName = fixAbsolutePath(checkNotBlank("name"));
        getParser().next(); // consume "file" tag
        FullFileModel fileModel = getFileModel(absoluteFileName);

        // loop thru the child elements, getting the "issue" ones
        while (skipToTag("issue")) {
            fileModel.addViolation(parseIssueElement());
        }
        endElement();
    }
    
    private Violation parseIssueElement()
            throws IOException, XmlPullParserException {
        
        Violation violation = new Violation();
        violation.setType(TYPE_NAME);
        violation.setLine(getString("line"));
        violation.setMessage(getString("reason"));
        violation.setSource(getString("evidence"));
        setSeverity(violation, getString("severity"));
        
        getParser().next();
        endElement();
        return violation;
    }

    private void setSeverity(Violation v, String severity) {
        v.setSeverity(SEVERITIES.get(severity));
        v.setSeverityLevel(Severity.getSeverityLevel(
                               v.getSeverity()));
    }
}