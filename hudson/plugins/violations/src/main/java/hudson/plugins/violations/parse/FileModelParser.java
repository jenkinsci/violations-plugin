package hudson.plugins.violations.parse;

import java.io.IOException;
import java.io.File;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import hudson.plugins.violations.model.FileModel;
import hudson.plugins.violations.model.Violation;

/**
 * Parses file model xml file.
 */
public class FileModelParser extends AbstractParser {

    private FileModel fileModel;

    /**
     * "Fluid" type method.
     * @param fileModel the fileModel value
     * @return this object.
     */
    public FileModelParser fileModel(FileModel fileModel) {
        this.fileModel = fileModel;
        return this;
    }

    /**
     * Parse the file violations xml file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute()
        throws IOException, XmlPullParserException {

        // Ensure that the top level tag is "file"
        expectNextTag("file");

        fileModel.setDisplayName(checkGetAttribute("name"));
        String file = getParser().getAttributeValue("", "file");
        if (file != null && !file.equals("")) {
            fileModel.setSourceFile(new File(file));
            fileModel.setLastModified(checkGetLong("last-modified"));
        }
        getParser().next(); // Consume "file" tag

        while (true) {
            String tag = getSibTag();
            if (tag == null) {
                break;
            }
            if (tag.equals("violation")) {
                parseViolationElement();
            } else if (tag.equals("line")) {
                parseLineElement();
            } else if (tag.equals("type")) {
                parseTypeElement();
            } else {
                skipTag();
            }
        }
        endElement();
    }

    private void parseViolationElement()
        throws IOException, XmlPullParserException {
        Violation ret = new Violation();
        ret.setLine(checkGetInt("line"));
        ret.setSource(checkGetAttribute("source"));
        ret.setSeverity(checkGetAttribute("severity"));
        ret.setSeverityLevel(getInt("severity-level"));
        ret.setType(checkGetAttribute("type"));
        ret.setMessage(checkGetAttribute("message"));
        String popup = getParser().getAttributeValue("", "popup-message");
        if (popup != null && !popup.equals("")) {
            ret.setPopupMessage(popup);
        }
        fileModel.addViolation(ret);
        getParser().next();
        endElement();
    }

    private void parseLineElement()
        throws IOException, XmlPullParserException {
        int lineNumber = checkGetInt("number");
        getParser().next(); // Skip the start tag
        String line = "";
        if (getParser().getEventType() == XmlPullParser.TEXT) {
            line = getParser().getText();
            getParser().next();
        }
        fileModel.getLines().put(lineNumber, line);
        endElement();
    }

    private void parseTypeElement()
        throws IOException, XmlPullParserException {
        String type = checkNotBlank("type");
        int    number = checkGetInt("number");
        int    suppressed = checkGetInt("suppressed");
        fileModel.addLimitType(type, number, suppressed);
        getParser().next(); // Skip the start tag
        endElement();
    }
}
