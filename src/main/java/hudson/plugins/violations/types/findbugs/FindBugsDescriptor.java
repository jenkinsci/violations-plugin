package hudson.plugins.violations.types.findbugs;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.parse.AbstractParser;
import hudson.plugins.violations.parse.AbstractTypeParser;
import hudson.plugins.violations.parse.ParseXML;
import hudson.plugins.violations.util.CloseUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

/**
 * The descriptor class for findbugs violations type.
 */
public final class FindBugsDescriptor extends TypeDescriptor {

    /**
     * Message resources
     *
     * <pre>
     * https://github.com/mebigfatguy/fb-contrib/blob/master/etc/messages.xml
     * https://code.google.com/p/findbugs/source/browse/findbugs/etc/messages.xml
     * </pre>
     */
    private static final String[] MESSAGES = new String[] {
            "findbugs-3.0.1.messages.xml", "fb-contrib-6.2.1.messages.xml", };
    /** The descriptor for the findbugs violations type. */
    public static final FindBugsDescriptor DESCRIPTOR = addDescriptor(new FindBugsDescriptor());

    private FindBugsDescriptor() {
        super("findbugs");
    }

    /**
     * Create a parser for the findbugs.
     *
     * @return a new findbugs parser.
     */
    @Override
    public AbstractTypeParser createParser() {
        return new FindBugsParser();
    }

    /**
     * Get a list of target xml files to look for for this particular type.
     *
     * @return a list filenames to look for in the target target directory.
     */
    @Override
    public List<String> getMavenTargets() {
        List<String> ret = new ArrayList<String>();
        ret.add("findbugsXml.xml"); // "good" report
        ret.add("findbugs.xml"); // "bad" report
        return ret;
    }

    private static Map<String, String> messageMap = new HashMap<String, String>();

    private static Map<String, String> detailMap = new HashMap<String, String>();

    /**
     * Get a detailed description of a violation source.
     *
     * @param source
     *            the code name of the violation.
     * @return a detailed description, if available, null otherwise.
     */
    @Override
    public String getDetailForSource(String source) {
        return detailMap.get(source);
    }

    /**
     * Get the map of cause to message.
     *
     * @return the map.
     */
    public static Map<String, String> getMessageMap() {
        return messageMap;
    }

    static {
        parseMessages();
    }

    private static void parseMessages() {
        for (String name : MESSAGES) {
            parseMessages(name);
        }
    }

    private static void parseMessages(String filename) {
        InputStream is = null;
        try {
            is = FindBugsParser.class.getResourceAsStream(filename);
            if (is != null) {
                ParseXML.parse(is, new ParseMessages());
            }
        } catch (Exception ex) {
            // Ignore
        } finally {
            CloseUtil.close(is);
        }
    }

    private static class ParseMessages extends AbstractParser {
        @Override
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
                toTag("Details");
                String details = getNextText("Details");
                detailMap.put(type, details);
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

}
