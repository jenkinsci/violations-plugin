package hudson.plugins.violations.parse;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


/**
 * Base class for parsing xml files.
 * Contains a number of utility protected
 * methods to aid in use of XmlPullParser.
 */
public abstract class AbstractParser {
    private XmlPullParser parser;

    /**
     * Get the parser.
     * @return the parser.
     */
    protected XmlPullParser getParser() {
        return parser;
    }

    /**
     * Set the parser.
     * @param parser the value to use.
     */
    public void setParser(XmlPullParser parser) {
        this.parser = parser;
    }

    /**
     * Abstract method to run the parsing.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected abstract void execute()
        throws IOException, XmlPullParserException;

    /**
     * Parse an input stream.
     * @param in the stream to parse.
     */
    /*package*/ void parse(InputStream in)
        throws IOException, XmlPullParserException {
        XmlPullParserFactory factory =  XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        parser = factory.newPullParser();

        parser.setInput(in, null);
        execute();
    }

    // -----------------------------------------------
    //
    //   Utility methods
    //
    // -----------------------------------------------

    /**
     * Get an attribute value as a string.
     * @param name the name of the attribute.
     * @return the int value of the attribute, or "" if the attribute
     *         is not present.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected String getString(String name)
        throws IOException, XmlPullParserException {
        String v = parser.getAttributeValue("", name);
        return (v == null)  ? "" : v;
    }

    /**
     * Get an attribute value as an int.
     * @param name the name of the attribute.
     * @return the int value of the attribute, or 0 if the attribute
     *         is not present or is not an int.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected int getInt(String name)
        throws IOException, XmlPullParserException {
        String v = parser.getAttributeValue("", name);
        try {
            return Integer.parseInt(v);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Get an attribute value as an int and ensure that
     * the attribute is present.
     * @param name the name of the attribute.
     * @return the int value of the attribute, or 0 if the attribute
     *         is not an int.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax
     *                                or the attribute is not present.
     */
    protected int checkGetInt(String name)
        throws IOException, XmlPullParserException {
        String v = checkGetAttribute(name);
        try {
            return Integer.parseInt(v);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Get an attribute value as a long and ensure that
     * the attribute is present.
     * @param name the name of the attribute.
     * @return the long value of the attribute, or 0 if the attribute
     *         is not an long.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax,
     *                                of the attribute is not present.
     */
    protected long checkGetLong(String name)
        throws IOException, XmlPullParserException {
        String v = checkGetAttribute(name);
        try {
            return Long.parseLong(v);
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Get an attribute value  and ensure that
     * the attribute is present.
     * @param name the name of the attribute.
     * @return the value of the attribute.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax,
     *                                or the attribute is not present.
     */
    protected String checkGetAttribute(String name)
        throws IOException, XmlPullParserException {
        String ret = parser.getAttributeValue("", name);
        if (ret == null) {
            throw new XmlPullParserException(
                "Expecting attribute "
                + name + " in element " + parser.getName());
        }
        return ret;
    }

    /**
     * Get an attribute value  and ensure that
     * the attribute is present and is not blank.
     * @param name the name of the attribute.
     * @return the value of the attribute.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     *  or the attribute is not present or is blank.
     */
    protected String checkNotBlank(String name)
        throws IOException, XmlPullParserException {
        String ret = parser.getAttributeValue("", name);
        if (ret == null || ret.trim().equals("")) {
            throw new XmlPullParserException(
                "Expecting attribute "
                + name + " in element " + parser.getName());
        }
        return ret;
    }

    /**
     * Skip to a specific tag within a element.
     * @param tagName the tag to look for.
     * @return true if the tag is found, false otherwise.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected boolean skipToTag(String tagName)
        throws IOException, XmlPullParserException {
        while (true) {
            if (parser.getEventType() == XmlPullParser.END_TAG) {
                return false;
            }
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            if (parser.getName().equals(tagName)) {
                return true;
            }
            skipTag();
        }
    }

    /**
     * get the next sibling element.
     * @return the tag of the element or none if there are no more
     *        elements.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected String getSibTag()
        throws IOException, XmlPullParserException {
        while (true) {
            if (parser.getEventType() == XmlPullParser.END_TAG) {
                return null;
            }
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                return parser.getName();
            }
            parser.next();
        }
    }


    /**
     * Assume at a start tag, skip it and the rest of the element.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected void skipTag()
        throws IOException, XmlPullParserException {
        parser.next();
        endElement();
    }

    /**
     * Find the next START_TAG event and throw an exception
     * if the tagname is not "tag";
     * @param tag the tag to expect.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected void expectStartTag(String tag)
        throws IOException, XmlPullParserException {
        while (true) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            if (parser.getName().equals(tag)) {
                return;
            }
        }
    }

    /**
     * Find the next START_TAG event and throw an exception
     * if the tagname is not "tag";
     * @param tag the tag to expect.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected void expectNextTag(String tag)
        throws IOException, XmlPullParserException {
        while (true) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                parser.next();
                continue;
            }
            if (parser.getName().equals(tag)) {
                return;
            }
            throw new IOException("Expecting tag " + tag);
        }
    }

    /**
     * check the current event type.
     * @param event the event type expected.
     * @param message a message to use in the RT exception if the
     *        event is not found.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected void checkEvent(int event, String message)
        throws IOException, XmlPullParserException {
        if (parser.getEventType() != event) {
            throw new RuntimeException(message);
        }
    }

    /**
     * get the next event and check the type.
     * @param event the event type expected.
     * @param message a message to use in the RT exception if the
     *        event is not found.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected void checkNextEvent(int event, String message)
        throws IOException, XmlPullParserException {
        parser.next();
        checkEvent(event, message);
    }

    /**
     * Get the next event and check it is a text event.
     * @param message a message to use in the RT exception if the
     *        TEXT event is not found.
     * @return the text in the event.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected String getNextText(String message)
        throws IOException, XmlPullParserException {
        checkNextEvent(XmlPullParser.TEXT, message);
        return parser.getText();
    }

    /**
     * skip to the end of the element.
     * recursivly skip over nested elements.
     * @throws IOException if there is a problem writing or reading.
     * @throws XmlPullParserException if there is a problem in the syntax.
     */
    protected void endElement()
        throws IOException, XmlPullParserException {
        // In a tag (after the tag has been consumed,
        // get and consume the end tag (recursively)
        while (true) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                parser.next();
                endElement();
                continue;
            }
            if (parser.getEventType() ==  XmlPullParser.END_TAG) {
                parser.next();
                return;
            }
            parser.next();
        }
    }
}
