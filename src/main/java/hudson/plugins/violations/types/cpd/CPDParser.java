package hudson.plugins.violations.types.cpd;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.parse.ViolationsDOMParser;

/**
 * Parses a cpd xml report file.
 */
public class CPDParser extends ViolationsDOMParser {

    private static final int LOW_LIMIT = 100;
    private static final int MEDIUM_LIMIT = 1000;

    /**
     * Parse the CPD file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute()
        throws IOException, Exception {
        Element docElement = getDocument().getDocumentElement();
        NodeList nl = docElement.getElementsByTagName("duplication");
        if (nl == null) {
            return;
        }
        for (int i = 0; i < nl.getLength(); ++i) {
            Element el = (Element) nl.item(i);
            parseDuplicationElement(el);
        }
    }

    private class FileElement {
        private int line;
        private String path;
    }

    private void parseDuplicationElement(Element parent)
        throws IOException, Exception {
        int lines = getInt(parent, "lines");
        int tokens = getInt(parent, "tokens");

        List<FileElement> fileElements = new ArrayList<FileElement>();
        // There should now be two or more file elements
        // and a codefragmentelement
        
        NodeList nl = parent.getElementsByTagName("file");
        if (nl == null) {
            return;
        }
        for (int i = 0; i < nl.getLength(); ++i) {
            Element el = (Element) nl.item(i);
            fileElements.add(parseFileElement(el));
        }

        FileElement prev = fileElements.get(
            fileElements.size() - 1);
        for (FileElement curr: fileElements) {
            addViolation(lines, tokens, curr, prev);
            prev = curr;
        }
    }

    private FileElement parseFileElement(Element parent)
        throws IOException, Exception {
        FileElement ret = new FileElement();
        ret.line =  getInt(parent, "line");
        ret.path = fixAbsolutePath(checkNotBlank(parent, "path").replace('\\', '/'));
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
