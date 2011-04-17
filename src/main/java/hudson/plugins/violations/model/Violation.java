package hudson.plugins.violations.model;

import hudson.plugins.violations.TypeDescriptor;

import static hudson.Util.*;

/**
 * A class contains information on a violation.
 */
public class Violation implements Comparable<Violation> {
    private int  line;
    private String  message;
    private String  popupMessage;
    private String  source;
    private int     severityLevel; // 0 is most serious
    private String  severity;      // the display name for the severity
    private String  type;

    /**
     * Set the type of violation ('checkstyle', 'pmd' etc).
     * @param type the value to use.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the type.
     * @return the type;
     */
    public String getType() {
        return type;
    }

    /**
     * Set the line number in that the violation occurred.
     * @param line the value to use.
     */
    public void setLine(int line) {
        this.line = line;
    }

    /**
     * Set the line number using a (possibly null) string.
     * If the string does not parse set the line to 0.
     * @param line the value to use.
     */
    public void setLine(String line) {
        try {
            this.line = Integer.parseInt(line);
        } catch (Exception ex) {
            this.line = 0;
        }
    }

    /**
     * Get the line number where the violation occurred.
     * @return the line number (0 if no line number).
     */
    public int getLine() {
        return line;
    }

    /**
     * Set the severityLevel of the violation.
     * @param severityLevel the value to use.
     */
    public void setSeverityLevel(int severityLevel) {
        this.severityLevel = severityLevel;
    }

    /**
     * Get the severityLevel number where the violation occurred.
     * @return the severityLevel number (0 if no severityLevel number).
     */
    public int getSeverityLevel() {
        return severityLevel;
    }

    /**
     * Set the severity string (NEEDS to be normalized).
     * @param severity the severity string.
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * Get the severity string (NEEDS to be normalized).
     * @return the severity string.
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Set the message to used when displaying the violation.
     * @param message the value to use.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the message to used when displaying the violation.
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message to used when displaying the violation
     * in a pop-up. If this is null use the message attribute.
     * This used for messages that contain markup like hyper links
     * that are not to appear in popups.
     * @param popupMessage the value to use.
     */
    public void setPopupMessage(String popupMessage) {
        this.popupMessage = popupMessage;
    }

    /**
     * Get the popup message.
     * @return the popup message.
     */
    public String getPopupMessage() {
        if (popupMessage == null) {
            return message;
        } else {
            return popupMessage;
        }
    }

    /**
     * Get the source detail message.
     * @return a detailed description for the source.
     */
    public String getSourceDetail() {
        String ret = TypeDescriptor.TYPES.get(type).getDetailForSource(
            source);
        if (ret == null) {
            ret = getPopupMessage();
        }
        return ret;
    }

    /**
     * Set the source of the violation.
     * This is normally a language independent code for
     * the violation.
     * @param source the value to use.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Get the source of the violation.
     * @return the source (from the last dot).
     */
    public String getSource() {
        int pos = source.lastIndexOf('.');
        if (pos != -1) {
            return source.substring(pos + 1);
        } else {
            return source;
        }
    }

    /**
     * Compare to another violation.
     * @param other the violation to compare to.
     * @return 0 if there are the same, otherwise base on the
     *           line number, type and hashcode.
     */
    public int compareTo(Violation other) {
        if (this == other) {
            return 0;
        }
        if (this.line < other.line) {
            return -1;
        } else if (this.line > other.line) {
            return 1;
        }
        int f = type.compareTo(other.type);
        if (f != 0) {
            return f;
        }
        f = severity.compareTo(other.severity);
        if (f != 0) {
            return f;
        }
        f = fixNull(message).compareTo(fixNull(other.message));
        if (f != 0) {
            return f;
        }
        if (hashCode() < other.hashCode()) {
            return -1;
        } else {
            return 1;
        }
    }
}
