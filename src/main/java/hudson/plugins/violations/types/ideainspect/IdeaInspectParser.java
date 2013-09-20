package hudson.plugins.violations.types.ideainspect;


import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.AbstractTypeParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


/**
 * Parser for parsing IDEA inspect reports.
 *
 * @author <a href="mailto:sarkhipov@asdco.ru">Sergey Arkhipov</a>
 */
public class IdeaInspectParser extends AbstractTypeParser {

    static final public String TYPE_NAME = "ideainspect";

    /**
     * Parse the IDEA report.
     *
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute() throws IOException, XmlPullParserException {
        try {
            expectNextTag("problems");
        } catch (IOException exc) {
            return;
        }

        getParser().next();

        while (skipToTag("problem")) {
            parseProblem();
        }
    }

    /**
     * Parses particular problem from a report.
     *
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    private void parseProblem()
        throws IOException, XmlPullParserException {
        getParser().next(); // consume "problem" tag

        String fileName = getFileName();
        int lineNumber = getLineNumber();
        Map<String, String> problemClass = getProblemClass();
        String description = getDescription();

        Violation violation = constructViolation(lineNumber, problemClass,
            description);

        FullFileModel fileModel = getFileModel(fileName);
        fileModel.addViolation(violation);

        endElement();
    }

    /**
     * Fetches file name from a problem section.
     *
     * @return File name appropriate for further usage with removed clutter.
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    private String getFileName() throws IOException, XmlPullParserException {
        skipToTag("file");

        String rawFileName = getText();
        rawFileName = rawFileName.replace("file://$PROJECT_DIR$/", "");
        rawFileName = resolveName(rawFileName);

        endElement();
        return rawFileName;
    }

    /**
     * Fetches line number from a problem section.
     *
     * @return Line number where problem was found.
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    private int getLineNumber() throws IOException, XmlPullParserException {
        skipToTag("line");

        try {
            return Integer.parseInt(getText());
        } catch (Exception exc) {
            throw new XmlPullParserException("Expecting integer line number");
        } finally {
            endElement();
        }
    }

    /**
     * Fetches short description on severity from a problem section.
     *
     * @return Data from the "problem_class" section.
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    private Map<String, String> getProblemClass() throws IOException,
        XmlPullParserException {
        skipToTag("problem_class");

        Map<String, String> problemClass = new HashMap<String, String>();
        problemClass.put("severity", checkNotBlank("severity"));
        problemClass.put("description", getText());

        endElement();
        return problemClass;
    }

    /**
     * Fetches full description on problem from a problem section.
     *
     * @return Full description of the problem.
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    private String getDescription() throws IOException, XmlPullParserException {
        skipToTag("description");

        String description = getText();

        endElement();
        return description;
    }

    /**
     * Constructs violation with a data from problem section.
     *
     * @param lineNumber - the number of the line problem was found
     * @param problemClass - map with description from "problem_class" section
     * @param description - full problem description
     * @return instance of violation.
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    private Violation constructViolation(int lineNumber,
        Map<String, String> problemClass, String description)
        throws IOException, XmlPullParserException {
        Violation violation = new Violation();
        violation.setType(TYPE_NAME);
        violation.setLine(lineNumber);
        violation.setSeverity(problemClass.get("severity"));
        violation.setMessage(problemClass.get("description"));
        violation.setSource("problem");
        violation.setPopupMessage(description);

        return violation;
    }

    /**
     * Fetches text from arbitrary text node.
     *
     * @return Text from a text node.
     * @throws java.io.IOException if there is a problem reading the file.
     * @throws org.xmlpull.v1.XmlPullParserException if there is a problem parsing the file.
     */
    private String getText() throws IOException, XmlPullParserException {
        return getParser().nextText();
    }
}
