package hudson.plugins.violations.types.jslint;

import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.AbstractTypeParser;
import hudson.plugins.violations.util.AbsoluteFileFinder;

import java.io.File;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

public class JsLintParser extends AbstractTypeParser {
    static final String TYPE_NAME = "jslint";
    
    private AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder();
    
    /**
     * Parse the JSLint xml file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute() throws IOException, XmlPullParserException {
        
        // ensure that the top level tag is "jslint"
        expectNextTag("jslint");
        getParser().next(); // consume the "jslint" tag
        
        absoluteFileFinder.addSourcePaths(getSourcePaths());
        absoluteFileFinder.addSourcePath(getProjectPath().getAbsolutePath());
        
        // loop thru the child elements, getting the "file" ones
        while (skipToTag("file")) {
            parseFileElement();
        }
    }

    private void parseFileElement()
        throws IOException, XmlPullParserException {

        String fileName = fixAbsolutePath(checkNotBlank("name"));
        File file = absoluteFileFinder.getFileForName(fileName);

        getParser().next(); // consume "file" tag
        FullFileModel fileModel = getFileModel(fileName, file);

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