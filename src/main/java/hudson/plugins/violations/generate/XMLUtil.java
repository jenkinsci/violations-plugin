package hudson.plugins.violations.generate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Some helper methods for encoding xml and html strings.
 */
public final class XMLUtil {
    private static final Logger LOG
        = Logger.getLogger(XMLUtil.class.getName());

    /** private constructor */
    private XMLUtil() {
        // Does nothing
        LOG.log(Level.FINE, "private constructor called");
    }

    /**
     * Convert a string to html content, Same as the xml version
     * except that spaces and tabs are converted.
     * @param tabIcon the icon to represent a tag.
     * @param str     the string to convert.
     * @return the converted string.
     */
    public static String escapeHTMLContent(String tabIcon, String str) {
        final StringBuilder b = new StringBuilder();
        final char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            switch (c) {
                case '<':
                    b.append("&lt;");
                    break;
                case '>':
                    b.append("&gt;");
                    break;
                case '&':
                    b.append("&amp;");
                    break;
                case ' ':
                    b.append("&nbsp;");
                    break;
                case '\t':
                    b.append("<img src='" + tabIcon + "' title='tab'/>");
                    b.append("&nbsp;");
                    break;
                default:
                    b.append(c);
            }
        }
        return b.toString();
    }

    /**
     * XML encode a string.
     * @param str the string to encode.
     * @return the encoded string.
     */
    public static String escapeContent(String str) {
        final StringBuilder b = new StringBuilder();
        final char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            switch (c) {
                case '<':
                    b.append("&lt;");
                    break;
                case '>':
                    b.append("&gt;");
                    break;
                case '&':
                    b.append("&amp;");
                    break;
                default:
                    b.append(c);
            }
        }
        return b.toString();
    }

    /**
     * Encode an attribute value.
     * This assumes use of " as the attribute value delimiter.
     * @param str the string to convert.
     * @return the converted string.
     */
    public static String escapeAttribute(String str) {
        final StringBuilder b = new StringBuilder();
        final char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            switch (c) {
                case '<':
                    b.append("&lt;");
                    break;
                case '>':
                    b.append("&gt;");
                    break;
                case '&':
                    b.append("&amp;");
                    break;
                case '"':
                    b.append("&quot;");
                    break;
                default:
                    b.append(c);
            }
        }
        return b.toString();
    }

    /**
     * Return an attribute setting.
     * @param name the name of the attribute.
     * @param value the value of the attribute.
     * @return the encoded attribute = value string.
     */
    public static String toAttribute(String name, String value) {
        return " " + name + "=\"" + escapeAttribute(value) + "\"";
    }

    /**
     * Return an attribute setting.
     * @param name the name of the attribute.
     * @param value the value of the attribute.
     * @return the encoded attribute = value string.
     */
    public static String toAttribute(String name, int value) {
        return " " + name + "=\"" + value + "\"";
    }
}
