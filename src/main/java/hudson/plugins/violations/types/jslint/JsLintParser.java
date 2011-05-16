package hudson.plugins.violations.types.jslint;

import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.AbstractTypeParser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

public class JsLintParser extends AbstractTypeParser {

    static final String TYPE_NAME = "jslint";
    
    /**
     * Parse the JSLint xml file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute() throws IOException, XmlPullParserException {
        
        // ensure that the top level tag is "jslint"
        expectNextTag("jslint");
        getParser().next(); // consume the "jslint" tag
        
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
        violation.setSeverity(Severity.MEDIUM);
        
        getParser().next();
        endElement();
        return violation;
    }
}