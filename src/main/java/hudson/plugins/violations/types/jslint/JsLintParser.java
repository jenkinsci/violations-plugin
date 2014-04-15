package hudson.plugins.violations.types.jslint;

import java.io.File;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;

import hudson.plugins.violations.parse.AbstractTypeParser;
import hudson.plugins.violations.util.AbsoluteFileFinder;

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
        
        absoluteFileFinder.addSourcePath(getProjectPath().getAbsolutePath());
        if (getSourcePaths() != null) {
            absoluteFileFinder.addSourcePaths(getSourcePaths());
        }

        // loop thru the child elements, getting the "file" ones
        while (skipToTag("file")) {
            parseFileElement();
        }
    }

    private void parseFileElement()
        throws IOException, XmlPullParserException {

        String relativeName = fixAbsolutePath(checkNotBlank("name"));
        File absoluteFile = absoluteFileFinder.getFileForName(relativeName);
        String absoluteFileName = absoluteFile == null ? relativeName : absoluteFile.getAbsolutePath();
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
