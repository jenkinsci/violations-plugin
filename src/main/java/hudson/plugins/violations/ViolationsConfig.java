package hudson.plugins.violations;

import java.util.TreeMap;
import java.io.Serializable;

/**
 * The configuration class for the violations plugin.
 */
public class ViolationsConfig implements Cloneable, Serializable {
    private static final int LIMIT_DEFAULT = 100;

    private TreeMap<String, TypeConfig> typeConfigs
        = new TreeMap<String, TypeConfig>();

    /** The number of violations per type per file to display */
    private int limit = LIMIT_DEFAULT;

    /** The (optional) source path pattern. */
    private String sourcePathPattern;

    /** The (optional) project base used for "faux" projects. */
    private String fauxProjectPath;

    /**
     * The constructor fot eh violations config.
     * This creates a config with default values.
     */
    public ViolationsConfig() {
        for (String type: TypeDescriptor.TYPES.keySet()) {
            typeConfigs.put(
                type, new TypeConfig(type));
        }
    }

    /**
     * Get the map of types to their config.
     * @return the type to config map.
     */
    public TreeMap<String, TypeConfig> getTypeConfigs() {
        return typeConfigs;
    }

    /**
     * Clone the config object.
     * @return a copy of this object.
     */
    public ViolationsConfig clone() {
        ViolationsConfig ret = new ViolationsConfig();
        for (String type: typeConfigs.keySet()) {
            // Filter out types that are not present anymore
            if (ret.typeConfigs.get(type) == null) {
                continue;
            }
            ret.typeConfigs.put(
                type, typeConfigs.get(type).clone());
        }
        ret.limit = limit;
        ret.sourcePathPattern = sourcePathPattern;
        ret.fauxProjectPath   = fauxProjectPath;
        return ret;
    }

    /**
     * Get the limit.
     * @return the limit of violations per type to display per file.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Set the limit.
     * @param limit the value to use.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Get the source path pattern.
     * This is used if one has a class but no source
     * file. This is true for the current maven find bugs plugin.
     * It may be true for other violation detectors as well.
     * @return the source path pattern.
     */
    public String getSourcePathPattern() {
        return sourcePathPattern;
    }

    /**
     * Set the source path patter.
     * @param sourcePathPattern the value to use.
     */
    public void setSourcePathPattern(String sourcePathPattern) {
        this.sourcePathPattern = sourcePathPattern;
    }

    /**
     * Get the faux project path.
     * This is used for projects that are not actually in
     * the hudson job directory area.
     * @return the faux project path - may be blank.
     */
    public String getFauxProjectPath() {
        return fauxProjectPath;
    }

    /**
     * Set the faux project path.
     * @param fauxProjectPath the value to use.
     */
    public void setFauxProjectPath(String fauxProjectPath) {
        this.fauxProjectPath = fauxProjectPath;
    }

    /**
     * After configuration change, reset the numbers
     * if they are out of range.
     */
    public void fix() {
        for (TypeConfig t:  typeConfigs.values()) {
            t.fix();
        }
        if (limit <= 0) {
            limit = LIMIT_DEFAULT;
        }
    }

    private static final long serialVersionUID = 1L;
}
