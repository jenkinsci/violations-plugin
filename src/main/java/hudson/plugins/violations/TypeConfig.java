package hudson.plugins.violations;

import hudson.plugins.violations.util.HealthNumber;
/**
 * This contains the configuration of a
 * particular violation type.
 */
public class TypeConfig implements Cloneable {
    /** Default min */
    public static final int DEFAULT_MIN = 10;
    /** Default max */
    public static final int DEFAULT_MAX = 999;

    private final String type;  // The type of this violation

    private int min = DEFAULT_MIN;  // Sunny report
    private int max = DEFAULT_MAX;  // Stormy report

    private String pattern = null; // File name pattern


    /**
     * Construct a type config.
     * @param type the type this config is for.
     */
    public TypeConfig(String type) {
        this.type = type;
    }

    /**
     * Return the type.
     * @return the type.
     */
    public String getType() {
        return type;
    }
    /**
     * Return the type.
     * @return the type.
     */
    public String getName() {
        return type;
    }

    /**
     * Get the max (stormy).
     * @return the lower limit for stormy.
     */
    public int getMax() {
        return max;
    }

    /**
     * Set the max (stormy).
     * @param max the lower limit for stormy
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Get the min (sunny).
     * @return the upper limit for sunny
     */
    public int getMin() {
        return min;
    }

    /**
     * Set the min (sunny).
     * @param min the upper limit for sunny.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Get the fileset pattern.
     * @return the pattern.
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Set the fileset pattern.
     * @param pattern the fileset pattern.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Ensure that max is &gt; min and that
     * both are positive.
     */
    public void fix() {
        if (min < 0) {
            min = 0;
        }
        if (max <= min) {
            max = min + 1;
        }
    }

    /**
     * A utility method to calculate the health percentage.
     * @param violations the number of violations.
     * @return the percentage according to the thresholds.
     */
    public int getHealthFor(int violations) {
        if (violations < 0) {
            // SPECIAL CASE (-1) means no xml violation files found
            return -1;
        }
        return new HealthNumber(min, max).calculate(violations);
    }


    /**
     * Clone the type config.
     * @return a cloned type config.
     */
    public TypeConfig clone() {
        try {
            return (TypeConfig) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex); // Should not happen
        }
    }
}
