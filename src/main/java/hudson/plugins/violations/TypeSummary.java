package hudson.plugins.violations;

import hudson.plugins.violations.model.Severity;

import java.io.Serializable;

/**
 * A summary report for a type.
 */
public class TypeSummary implements Serializable {

    private String errorMessage;
    private int[] severityArray =
        new int[Severity.LOW_VALUE + 1];

    /**
     * Get the error message for the type - if any.
     * @return the error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Set the error message for the type - if any.
     * @param errorMessage the message to set.
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Get the severity array.
     * @return the severity array.
     */
    public int[] getSeverityArray() {
        return severityArray;
    }

    private static final long serialVersionUID = 1L;
}

