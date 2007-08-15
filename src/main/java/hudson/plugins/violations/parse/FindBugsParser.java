package hudson.plugins.violations.parse;

import java.io.IOException;
import java.io.File;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.util.StringUtil;
import hudson.plugins.violations.util.HashMapWithDefault;
import hudson.plugins.violations.model.Violation;

/**
 * Parses a find bugs xml report file.
 */
public class FindBugsParser extends AbstractTypeParser {
    private static final Logger LOG = Logger.getLogger(
        FindBugsParser.class.getName());

    private boolean debug = false;
    private List<String> sourceDirectories
        = new ArrayList<String>();

    /**
     * Parse the findbugs xml file.
     * @throws IOException if there is a problem reading the file.
     * @throws XmlPullParserException if there is a problem parsing the file.
     */
    protected void execute()
        throws IOException, XmlPullParserException {
        // Ensure that the top level tag is "BugCollection"
        expectNextTag("BugCollection");
        getParser().next(); // consume the "BugCollection" tag

        // Initial populate the source directories
        for (String d: getSourcePaths()) {
            sourceDirectories.add(d);
        }

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

    private String resolveClassName(String classname) {
        if (classname == null) {
            return null;
        }
        String filename = classname.replace('.', '/');
        int pos = filename.indexOf('$');
        if (pos != -1) {
            filename = filename.substring(0, pos);
        }
        return filename + ".java";
    }

    private File getFileForName(String name) {
        if (debug) {
            System.out.println("getFileForName(" + name + ")");
        }
        for (String p: sourceDirectories) {
            File f = new File(new File(p), name);
            if (debug) {
                System.out.println("Checking " + f);
            }
            if (f.exists()) {
                if (debug) {
                    System.out.println("FOUND !!");
                }
                return f;
            }
        }
        if (debug) {
            System.out.println("Not found");
        }
        return null;
    }

    private String getRelativeName(String name, File file) {
        if (file != null && file.exists()) {
            String absolute = file.getAbsolutePath();
            String relative = resolveName(absolute);
            if (!relative.equals(absolute)) {
                return relative;
            }
        }
        return name;
    }

    private void getMavenFileInstance()
        throws IOException, XmlPullParserException {
        // FIXME: I have no definition - just an example file
        //        with no source files
        String classname = checkNotBlank("classname");
        String name = resolveClassName(classname);
        File file = getFileForName(name);
        name = getRelativeName(name, file);
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
                v.setSeverity(normalizeSeverity(
                                  checkNotBlank("priority")));
                v.setSeverityLevel(
                    getSeverityLevel(v.getSeverity()));
                v.setMessage(checkNotBlank("message"));
                getFileModel(name, file).addViolation(v);
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
        String y = FindBugsDescriptor.getMessageMap().get(x);
        return (y == null) ? x : y;
    }

    /**
     * Parse the BugInstance element.
     * -- 1.2.1 --
     * This is something like:
     *  BugInstance {
     *     Class [classname] { SourceLine? (of the class) } ?,
     *     Method { SourceLine?  (of the method) } ?,
     *     Field ?,
     *     Type*,
     *     LocalVariable*
     *     SourceLine* (of the line in error)
     *  }
     * This code will look classname and the SourceLine element.
     * The last source line element will be used for the line number.
     */
    private String classname;
    private String path;
    private String lineNumber;

    private void getSourceLine()
        throws IOException, XmlPullParserException {
        if (StringUtil.isBlank(path)) {
            path      =  getParser().getAttributeValue("", "sourcepath");
        }
        if (StringUtil.isBlank(classname)) {
            classname =  getParser().getAttributeValue("", "classname");
        }
        String l = getParser().getAttributeValue("", "start");
        if (!StringUtil.isBlank(l)) {
            lineNumber = l;
        }
    }

    private void getSourceLines()
        throws IOException, XmlPullParserException {
        while (true) {
            String tag = getSibTag();
            if (tag == null) {
                return;
            }
            if ("SourceLine".equals(tag)) {
                getSourceLine();
            }
            skipTag();
        }
    }

    private boolean sameClassname(String currentClassname) {
        if (currentClassname == null) {
            return true;
        }
        String thisClassname = getParser().getAttributeValue("", "classname");
        if (StringUtil.isBlank(thisClassname)) {
            return true;
        }
        return thisClassname.equals(currentClassname);
    }

    private void getBugInstance()
        throws IOException, XmlPullParserException {
        String type = getParser().getAttributeValue("", "type");
        String priority = getParser().getAttributeValue("", "priority");
        String category = getParser().getAttributeValue("", "category");
        getParser().next();

        classname = null;
        path = null;
        lineNumber = null;

        while (true) {
            String tag = getSibTag();
            if (tag == null) {
                break;
            }
            if (("Class".equals(tag) || "Method".equals(tag)
                || "Field".equals(tag))
                && sameClassname(classname)) {
                if (StringUtil.isBlank(classname)) {
                    classname = getParser().getAttributeValue("", "classname");
                }
                getParser().next();
                getSourceLines();
                endElement();
                continue;
            } else if ("SourceLine".equals(tag)) {
                getSourceLine();
            }
            skipTag();
        }

        String name = path == null ? resolveClassName(classname) : path;
        if (StringUtil.isBlank(name)) {
            LOG.info("Unable to decode BugInstance element");
            endElement();
            return;
        }

        File file = getFileForName(name);

        name = getRelativeName(name, file);
        Violation v = new Violation();
        v.setType("findbugs");
        v.setLine(lineNumber);
        v.setSource(type);
        v.setSeverity(normalizeSeverity(priority));
        v.setSeverityLevel(
            getSeverityLevel(v.getSeverity()));
        v.setMessage(convertType(type));
        getFileModel(name, file).addViolation(v);

        endElement();
    }

    private static final HashMapWithDefault<String, String> SEVERITIES
        = new HashMapWithDefault<String, String>("High");

    static {
        // From findbugs
        SEVERITIES.put("1", "High");
        SEVERITIES.put("2", "Medium");
        SEVERITIES.put("3", "Low");

        // From maven
        SEVERITIES.put("High", "High");
        SEVERITIES.put("Medium", "Medium");
        SEVERITIES.put("Low", "Low");
    }

    /** Convert a severity in the xml to a name */
    private String normalizeSeverity(String name) {
        return SEVERITIES.get(name);
    }

    /** Convert a normalized severity to a severity level */
    private int getSeverityLevel(String severity) {
        return Severity.getSeverityLevel(
            severity);
    }
}
