package hudson.plugins.violations.model;

import hudson.plugins.violations.util.HashMapWithDefault;

/**
 * Severity levels of violations.
 * These are normalized from the
 * various violation types.
 * PMD was used as the base for the names.
 */
public final class Severity {
    private static final HashMapWithDefault<String, Integer> LEVELS
        = new HashMapWithDefault<String, Integer>(0);

    /** Map of number to string */
    public static final String[] NAMES;

    /** High severity name */
    public static final String HIGH = "High";
    /** High severity value */
    public static final int HIGH_VALUE = 0;

    /** Medium High severity name */
    public static final String MEDIUM_HIGH = "Medium High";
    /** Medium High severity value */
    public static final int MEDIUM_HIGH_VALUE = 1;

    /** Medium severity name */
    public static final String MEDIUM = "Medium";
    /** Medium severity value name */
    public static final int MEDIUM_VALUE = 2;

    /** Medium Low severity name */
    public static final String MEDIUM_LOW = "Medium Low";
    /** Medium Low severity value */
    public static final int MEDIUM_LOW_VALUE = 3;

    /** Low severity name */
    public static final String LOW = "Low";
    /** Low severity value*/
    public static final int LOW_VALUE = 4;

    /** Number of severity */
    public static final int NUMBER_SEVERITIES = 5;

    /** Private constructor */
    private Severity() {
    }

    /**
     * Get the level for a particular normalized severity.
     * @param severity the value to convert.
     * @return the level.
     */
    public static int getSeverityLevel(String severity) {
        return LEVELS.get(severity);
    }

    static {
        LEVELS.put(HIGH, HIGH_VALUE);
        LEVELS.put(MEDIUM_HIGH, MEDIUM_HIGH_VALUE);
        LEVELS.put(MEDIUM, MEDIUM_VALUE);
        LEVELS.put(MEDIUM_LOW, MEDIUM_LOW_VALUE);
        LEVELS.put(LOW, LOW_VALUE);
        NAMES =  new String[] {
            HIGH, MEDIUM_HIGH, MEDIUM, MEDIUM_LOW, LOW};
    }
}