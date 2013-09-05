package hudson.plugins.violations.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.SortedMap;

import java.io.File;

/**
 * An base class containing attributes
 * common to FullFileModel and FileModel.
 */
@MappedSuperclass
abstract class AbstractFileModel {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String displayName;
    @Column
    private File   sourceFile;
    @Column
    private long   lastModified;

    @Transient
    private Map<Integer, Set<Violation>> lineViolationMap
        = new TreeMap<Integer, Set<Violation>>();
    @Transient
    private SortedMap<Integer, String> lines
        = new TreeMap<Integer, String>();
    @Transient
    private TreeMap<String, TreeSet<Violation>> typeMap
        = new TreeMap<String, TreeSet<Violation>>();


    @OneToMany(orphanRemoval=true)
    @JoinColumn(name="file")
    private Collection<Violation> getViolations() {
        Collection<Violation> r = new TreeSet<Violation>();
        for (Set<Violation> s : getLineViolationMap().values()) {
            r.addAll(s);
        }
        return r;
    }

    private void setViolations(Collection<Violation> violations) {
        for (Violation v : violations) {
            addViolation(v);
        }
    }

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
