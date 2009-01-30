package hudson.plugins.violations.model;

import hudson.plugins.violations.util.Equals;
import hudson.plugins.violations.util.HashCodes;

/**
 * Supppres a violation.
 * Can be at a project or at a file level.
 */
public class Suppression {
    private String type;
    private String source;
    private String message;
    private String reason;
    private String fileName;

    public Suppression(
        String type, String source, String fileName,
        String reason, String message) {
        this.type = type;
        this.source = source;
        this.message = message;
        this.fileName = fileName;
        this.reason = reason;
    }

    public String getType() {
        return type;
    }
    public String getSource() {
        return source;
    }
    public String getFileName() {
        return fileName;
    }
    public String getMessage() {
        return message;
    }
    public String getReason() {
        return reason;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        Suppression other = (Suppression) o;
        return Equals.equals(
            type, other.type, source, other.source, fileName, other.fileName);
    }

    @Override
    public int hashCode() {
        return HashCodes.hashCode(type, source, fileName);
    }
}
