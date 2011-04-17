package hudson.plugins.violations.model;

import java.util.TreeSet;

/**
 * A set of violations from a full file model for a
 * particular type.
 */
public class FileSummary implements Comparable<FileSummary> {
    private final FullFileModel fileModel;
    private final TreeSet   violations;

    /**
     * Constructor for FileSummary.
     * @param fileModel the file model
     * @param violations the violation set to associate with the file model.
     */
    public FileSummary(FullFileModel fileModel, TreeSet violations) {
        this.fileModel = fileModel;
        this.violations = violations;
    }

    /**
     * Get the file model.
     * @return the full file model.
     */
    public FullFileModel getFileModel() {
        return fileModel;
    }

    /**
     * Get the associated violations.
     * @return the violations.
     */
    public TreeSet getViolations() {
        return violations;
    }

    /**
     * Implement a compare to based on the number of violations.
     * @param other the other summary to compare to.
     * @return 0 if same, 1 if less and -1 if greater (need most at top)
     */
    public int compareTo(FileSummary other) {
        if (this == other) {
            return 0;
        }
        if (violations.size() > other.violations.size()) {
            return -1;
        } else if (violations.size() < other.violations.size()) {
            return 1;
        }
        return fileModel.getDisplayName().compareTo(
            other.fileModel.getDisplayName());
    }
}

