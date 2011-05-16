package hudson.plugins.violations.types.codenarc;

import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.AbstractTypeParser;
import hudson.plugins.violations.util.HashMapWithDefault;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Parses a codenarc xml report file.
 * @author Robin Bramley, Opsera Ltd.
 */
public class CodenarcParser extends AbstractTypeParser {

    private static final HashMapWithDefault<String, String> SEVERITIES
        = new HashMapWithDefault<String, String>(Severity.MEDIUM);

    static {
        SEVERITIES.put("1", Severity.HIGH);
        SEVERITIES.put("2", Severity.MEDIUM);
        SEVERITIES.put("3", Severity.LOW);
    }

    /**
     * Parse the codenarc xml file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute()
        throws IOException, XmlPullParserException {

        // Ensure that the top level tag is "CodeNarc"
        expectNextTag("CodeNarc");
        getParser().next(); // consume the "CodeNarc" tag
        // loop through the child elements, getting the "file" ones
        while (skipToTag("Package")) {
            String path = checkNotBlank("path");

            getParser().next();
            while (skipToTag("File")) {
                parseFileElement(path);
            }
            endElement();
        }
        endElement(); // CodeNarc
    }

/*
<Package path='grails-app/controllers' totalFiles='30' filesWithViolations='3' priority1='0' priority2='2' priority3='3'>
  <File name='LoginController.groovy'>
    <Violation ruleName='UnusedImport' priority='3' lineNumber='2'>
      <SourceLine><![CDATA[import org.grails.plugins.springsecurity.service.AuthenticateService]]></SourceLine>
    </Violation>
  </File>
  <File name='RegisterController.groovy'>
    <Violation ruleName='UnusedImport' priority='3' lineNumber='4'>
      <SourceLine><![CDATA[import org.springframework.security.providers.UsernamePasswordAuthenticationToken as AuthToken]]></SourceLine>
    </Violation>
    <Violation ruleName='UnusedImport' priority='3' lineNumber='5'>
      <SourceLine><![CDATA[import org.springframework.security.context.SecurityContextHolder as SCH]]></SourceLine></Violation>
    <Violation ruleName='GrailsPublicControllerMethod' priority='2' lineNumber='226'>
      <SourceLine><![CDATA[def sendValidationEmail(def person) {]]></SourceLine>
    </Violation>
    <Violation ruleName="AbcComplexity" priority="2" lineNumber="">
      <Message><![CDATA[The ABC score for method [foobar] is [92.0]]]></Message>
    </Violation>
  </File>
</Package>
*/

    /**
     * Handle a Codenarc File element.
     */
    private void parseFileElement(String path)
        throws IOException, XmlPullParserException {

        //TODO: This doesn't always work if files are under e.g. workspace/trunk [workaround is to use the faux project path]
        String absoluteFileName = fixAbsolutePath(getProjectPath().getAbsolutePath() + "/" + path + "/" + checkNotBlank("name"));
        getParser().next();  // consume "file" tag
        FullFileModel fileModel = getFileModel(absoluteFileName);

        // Loop through the child elements, getting the violations
        while (skipToTag("Violation")) {
            fileModel.addViolation(parseViolationElement());
            endElement();
        }
        endElement();
    }

    /**
     * Convert a Codenarc violation to a Hudson Violation.
     * @return Violation
     */
    private Violation parseViolationElement()
        throws IOException, XmlPullParserException {

        Violation ret = new Violation();
        ret.setType("codenarc");
        ret.setLine(getString("lineNumber"));
        ret.setSource(getString("ruleName"));
        setSeverity(ret, getString("priority"));
        getParser().next();


        // get the contents of the embedded SourceLine or Message element
        try {
            expectNextTag("SourceLine");
            getNextText("Missing SourceLine"); // ignored
            // no message --- use the rule name as the default, which is the most descriptive
            ret.setMessage(ret.getSource());
        } catch (IOException ioe) {
            expectNextTag("Message");
            ret.setMessage(getNextText("Missing Message"));
        }
        //TODO: the following depends upon a patch to CodeNarc 0.9 - so should be exception tolerant
        // get the contents of the embedded Description element
        //expectNextTag("Description");
        //ret.setPopupMessage(getNextText("Missing Description"));

        getParser().next();
        endElement();
        return ret;
    }

    /**
     * Map severities from Codenarc to Hudson.
     */
    private void setSeverity(Violation v, String severity) {
        v.setSeverity(SEVERITIES.get(severity));
        v.setSeverityLevel(Severity.getSeverityLevel(
                               v.getSeverity()));
    }
}
