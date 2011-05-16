package hudson.plugins.violations.types.jcreport;

import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.AbstractTypeParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

/**
 * Parses a jcreport xml report file.
 * @author Andreas.Mandel@gmail.com
 */
public class JcReportParser extends AbstractTypeParser {

    private static final Map<String, String> SEVERITIES
        = new HashMap<String, String>();

    // see
    static {
        SEVERITIES.put("error", Severity.HIGH);
        SEVERITIES.put("cpd", Severity.MEDIUM_HIGH);
        SEVERITIES.put("warning", Severity.MEDIUM);
        SEVERITIES.put("design", Severity.MEDIUM_LOW);
        SEVERITIES.put("code-style", Severity.LOW);
        SEVERITIES.put("info", null);
        SEVERITIES.put("coverage", null);
        SEVERITIES.put("ok", null);
        SEVERITIES.put("filtered", null);
    }


    /**
     * Parse the jcreport xml file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute()
        throws IOException, XmlPullParserException
    {

        // Ensure that the top level tag is "report"
        expectNextTag("report");
        getParser().next(); // consume the "report" tag
        // loop through the child elements, getting the "file" ones
        while (skipToTag("file"))
        {
            parseFileElement();
        }
        endElement(); // report
    }

    private void parseFileElement()
        throws IOException, XmlPullParserException
    {

        final String name = getString("name");
        getParser().next();  // consume "file" tag
        if (null != name && name.length() > 0)
        {
          FullFileModel fileModel = null;

          // Loop through the child elements, getting the "item" ones
          while (skipToTag("item"))
          {
              final Violation violation = parseItemElement();
              if (null != violation)
              {
                  if (fileModel == null)
                  {
                    fileModel = getFileModel(fixAbsolutePath(name));
                  }
                  fileModel.addViolation(parseItemElement());
              }
              getParser().next();
              endElement();
          }
        }
        endElement();
    }

    private Violation parseItemElement()
        throws IOException, XmlPullParserException
    {
        final String severity = SEVERITIES.get(getString("severity"));
        Violation ret = null;
        if (severity != null)
        {
            ret = new Violation();
            ret.setType("jcreport");
            // ret.setType(getString("origin")); // report the real source?
            ret.setLine(getString("line"));
            ret.setMessage(getString("message"));
            ret.setSource(getString("finding-type"));
            ret.setSeverity(severity);
            ret.setSeverityLevel(Severity.getSeverityLevel(severity));
        }
        return ret;
    }
}
