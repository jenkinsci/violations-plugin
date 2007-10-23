package hudson.plugins.violations.types.cpd;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;


import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.AbstractTypeParser;

/**
 * Parses a cpd xml report file.
 */
public class CPDParser extends AbstractTypeParser {

    private static final int LOW_LIMIT = 100;
    private static final int MEDIUM_LIMIT = 1000;

    /**
     * Parse the CPD file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute()
        throws IOException, XmlPullParserException {

        // Ensure that the top level tag is "pmd-cpd"
        expectNextTag("pmd-cpd");
        getParser().next(); // consume the "pmd-cpd" tag
        // loop tru the child elements, getting the "file" ones
        while (skipToTag("duplication")) {
            parseDuplicationElement();
        }
    }

    private class FileElement {
        private int line;
        private String path;
    }

    private void parseDuplicationElement()
        throws IOException, XmlPullParserException {

        int lines = getInt("lines");
        int tokens = getInt("tokens");
        getParser().next();  // consume "duplication" tag
        List<FileElement> fileElements = new ArrayList<FileElement>();
        // There should now be two or more file elements
        // and a codefragmentelement
        while (skipToTag("file")) {
            fileElements.add(parseFileElement());
        }

        FileElement prev = fileElements.get(
            fileElements.size() - 1);
        for (FileElement curr: fileElements) {
            addViolation(lines, tokens, curr, prev);
            prev = curr;
        }
        endElement();
    }

    private FileElement parseFileElement()
        throws IOException, XmlPullParserException {
        // Ensure that the next tag is "file"
        expectNextTag("file");
        FileElement ret = new FileElement();
        ret.line =  getInt("line");
        ret.path = checkNotBlank("path").replace('\\', '/');
        getParser().next();
        endElement();
        return ret;
    }

    private static String relativePath(final String self, final String other) {
        if (self.equals(other)) {
            return "";
        }

        String[] selfParts = self.split("/");
        String[] otherParts = other.split("/");
        int len = selfParts.length;
        if (otherParts.length < len) {
            len = otherParts.length;
        }

        int same = 0; // used outside for loop
        for (; same < len; same++) {
            if (!selfParts[same].equals(otherParts[same])) {
                break;
            }
        }
        StringBuilder b = new StringBuilder();
        for (int i = same + 1; i < selfParts.length; ++i) {
            b.append("../");
        }
        boolean first = true;
        for (int i = same; i < otherParts.length; ++i) {
            if (!first) {
                b.append("/");
            }
            first = false;
            b.append(otherParts[i]);
        }
        return b.toString();
    }

    private String relativePoint(
        FileElement self, FileElement other) {
        String path = relativePath(self.path + "/bats", other.path);
        return path + "#line" + other.line;
    }

    private String relativeOther(
        FileElement self, FileElement other) {
        StringBuilder b = new StringBuilder();
        b.append("line " + other.line);
        if (!self.path.equals(other.path)) {
            b.append(" in ");
            b.append(other.path.substring(other.path.lastIndexOf("/") + 1));
        }
        return b.toString();
    }

    private String relativeHRef(
        FileElement self, FileElement other) {
        StringBuilder b = new StringBuilder();
        b.append("<a href='");
        b.append(relativePoint(self, other));
        b.append("'>");
        b.append(relativeOther(self, other));
        b.append("</a>");
        return b.toString();
    }

    private void addViolation(int lines, int tokens, FileElement self,
                              FileElement other) {
        Violation v = new Violation();
        v.setType("cpd");
        v.setLine(self.line);
        setSeverity(
            v,
            (tokens < LOW_LIMIT) ? Severity.LOW
            : (tokens < MEDIUM_LIMIT) ? Severity.MEDIUM
            : Severity.HIGH);
        v.setSource("duplication");
        v.setMessage(
            "Duplication of " + tokens + " tokens from "
            + relativeHRef(self, other));
        v.setPopupMessage(
            "Duplication of " + tokens + " tokens from "
            + relativeOther(self, other));
        getFileModel(self.path).addViolation(v);
    }

    private void setSeverity(Violation v, String severity) {
        v.setSeverity(severity);
        v.setSeverityLevel(Severity.getSeverityLevel(
                               severity));
    }
}
