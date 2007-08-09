package hudson.plugins.violations.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import hudson.plugins.violations.util.CloseUtil;
import hudson.plugins.violations.model.Violation;

/**
 * Parses a find bugs xml report file.
 */
public class FindBugsParser extends AbstractTypeParser {

    private List<String> sourceDirectories
        = new ArrayList<String>();

    private static Map<String, String> messageMap;

    private static class ParseMessages extends AbstractParser {
        public void execute() {
            try {
                doIt();
            } catch (Exception ex) {
                // ignore
            }
        }
        private void doIt() throws Exception {
            while (true) {
                toTag("BugPattern");
                String type = checkNotBlank("type");
                toTag("ShortDescription");
                String desc = getNextText("missin");
                messageMap.put(type, desc);
            }
        }
        private void toTag(String name) throws Exception {
            while (true) {
                if (getParser().getEventType() == XmlPullParser.START_TAG) {
                    if (getParser().getName().equals(name)) {
                        return;
                    }
                }
                getParser().next();
            }
        }
    }


    private static void parseMessages() {
        if (messageMap != null) {
            return;
        }
        messageMap = new HashMap<String, String>();

        InputStream is = null;
        try {
            is = FindBugsParser.class.getResourceAsStream(
                "FindBugsParser/messages.xml");
            if (is != null) {
                ParseXML.parse(is, new ParseMessages());
            }
        } catch (Exception ex) {
            // Ignore
        } finally {
            CloseUtil.close(is);
        }
    }

    /**
     * Parse the findbugs xml file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute()
        throws IOException, XmlPullParserException {
        parseMessages();

        // Ensure that the top level tag is "BugCollection"
        expectNextTag("BugCollection");
        getParser().next(); // consume the "BugCollection" tag

        // Top level tags:
        //   file: from the maven findbugs plugin
        //   Project: from the ant findbugs tag
        //   BugInstance: from the ant findbugs tag
        //   others - ignore
        while (true) {
            String tag = getSibTag();
            if (tag == null) {
                break;
            }
            if (tag.equals("Project")) {
                getSourceDirs();
            } else if (tag.equals("BugInstance")) {
                getBugInstance();
            } else if (tag.equals("file")) {
                getMavenFileInstance();
            } else {
                skipTag();
            }
        }
    }

    private void getMavenFileInstance()
        throws IOException, XmlPullParserException {
        // FIXME: I have no definition - just an example file
        //        with no source files
        String classname = checkNotBlank("classname");
        String name = classname;

        getParser().next();
        while (true) {
            String tag = getSibTag();
            if (tag == null) {
                break;
            }
            if (tag.equals("BugInstance")) {
                Violation v = new Violation();
                v.setType("findbugs");
                v.setLine(getInt("lineNumber"));
                v.setSource(checkNotBlank("type"));
                v.setSeverity(checkNotBlank("priority"));
                v.setMessage(checkNotBlank("message"));
                getFileModel(name).addViolation(v);
            }
            skipTag();
        }
        endElement();
    }

    private void getSourceDirs()
        throws IOException, XmlPullParserException {
        expectStartTag("Project");
        getParser().next();
        while (skipToTag("SrcDir")) {
            getSourceDir();
        }
        endElement();
    }

    private void getSourceDir()
        throws IOException, XmlPullParserException {
        checkNextEvent(XmlPullParser.TEXT, "Expecting text");
        sourceDirectories.add(getParser().getText());
        endElement();
    }

    private void getBugInstances()
        throws IOException, XmlPullParserException {
        while (skipToTag("BugInstance")) {
            getBugInstance();
        }
    }

    private String convertType(String x) {
        if (messageMap == null) {
            return x;
        }
        String y = messageMap.get(x);
        return (y == null) ? x : y;
    }

    private void getBugInstance()
        throws IOException, XmlPullParserException {
        String type = getParser().getAttributeValue("", "type");
        String priority = getParser().getAttributeValue("", "priority");
        String category = getParser().getAttributeValue("", "category");
        String path = null;
        String classname = null;
        String lineNumber = null;

        getParser().next();
        while (skipToTag("SourceLine")) {
            path      =  getParser().getAttributeValue("", "sourcepath");
            classname =  getParser().getAttributeValue("", "classname");
            lineNumber = getParser().getAttributeValue("", "start");
            skipTag();
        }
        String name = path == null ? classname : path;
        File file = null;
        for (String p: sourceDirectories) {
            File f = new File(new File(p), name);
            if (f.exists()) {
                file = f;
                break;
            }
        }

        if (name != null) {

            if (file != null && file.exists()) {
                String absolute = file.getAbsolutePath();
                String relative = resolveName(absolute);
                if (!relative.equals(absolute)) {
                    name = relative;
                }
            }

            Violation v = new Violation();
            v.setType("findbugs");
            v.setLine(lineNumber);
            v.setSource(type);
            v.setSeverity(priority);
            v.setMessage(convertType(type));
            getFileModel(name, file).addViolation(v);
        }
        endElement();
    }
}
