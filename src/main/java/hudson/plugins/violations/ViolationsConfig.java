package hudson.plugins.violations;

import java.util.TreeMap;

/**
 * The configuration class for the violations plugin.
 */
public class ViolationsConfig implements Cloneable {
    private static final int LIMIT_DEFAULT = 100;

    private TreeMap<String, TypeConfig> typeConfigs
        = new TreeMap<String, TypeConfig>();

    /** The number of violations per type per file to display */
    private int limit = LIMIT_DEFAULT;

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
}
