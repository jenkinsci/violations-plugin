package hudson.plugins.violations.model;

import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.SortedMap;

import java.io.File;

/**
 * An base class containing attributes
 * common to FullFileModel and FildModel.
 */
abstract class AbstractFileModel {

    private String displayName;
    private File   sourceFile;
    private long   lastModified;
    private Map<Integer, Set<Violation>> lineViolationMap
        = new TreeMap<Integer, Set<Violation>>();
    private SortedMap<Integer, String> lines
        = new TreeMap<Integer, String>();
    private TreeMap<String, TreeSet<Violation>> typeMap
        = new TreeMap<String, TreeSet<Violation>>();

    /**
     * Get the display name of this file.
     * @return the name to use whrn displaying the file violations.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Set thr name to use to display this file.
     * @param name the name to use.
     */
    public void setDisplayName(String name) {
        this.displayName = name;
    }

    /**
     * Get the source file.
     * @return the source file.
     */
    public File getSourceFile() {
        return sourceFile;
    }

    /**
     * Set the source file.
     * @param sourceFile the value to use.
     */
    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Set the last modified time of the file.
     * @param lastModified the time to use.
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Get the last modified time of the file.
     * @return  the lastModified time.
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Get a map of line number to a set of violations.
     * @return the map.
     */
    public  Map<Integer, Set<Violation>> getLineViolationMap() {
        return lineViolationMap;
    }

    /**
     * Get a sorted map of lines.
     * @return a line number to line content map.
     */
    public SortedMap<Integer, String> getLines() {
        return lines;
    }

    /**
     * Get the map of types to violations.
     * @return the type to violation map.
     */
    public TreeMap<String, TreeSet<Violation>> getTypeMap() {
        return typeMap;
    }

    /**
     * Add a violation to the file model.
     * @param v the violation to add.
     */
    public abstract void addViolation(Violation v);

}
