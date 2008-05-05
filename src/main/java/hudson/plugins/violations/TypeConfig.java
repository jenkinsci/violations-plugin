package hudson.plugins.violations;

import hudson.plugins.violations.util.HealthNumber;

import java.io.Serializable;

/**
 * This contains the configuration of a
 * particular violation type.
 */
public class TypeConfig implements Cloneable, Serializable {
    /** Default min */
    public static final int DEFAULT_MIN = 10;
    /** Default max */
    public static final int DEFAULT_MAX = 999;
    /** Default unstable */
    public static final int DEFAULT_UNSTABLE = 999;

    /** Default fail */
    public static final int DEFAULT_FAIL = 99999;

    private final String type;  // The type of this violation

    private int min = DEFAULT_MIN;  // Sunny report
    private int max = DEFAULT_MAX;  // Stormy report
    private Integer unstable = DEFAULT_UNSTABLE; // unstable 
    private Integer fail = null;
    private boolean usePattern = false; // for maven2

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
     * Get the unstable value
     * @return the unstable value
     */
    public int getUnstable() {
        if (unstable == null) { // OLD CONFIG
            unstable = DEFAULT_UNSTABLE;
        }   
        return unstable;
    }

    /**
     * Set the unstable value.
     * @param unstable the value to use.
     */
    public void setUnstable(int unstable) {
        this.unstable = unstable;
    }
 

    /**
     * Set the fail value.
     * @param fail the value to use.
     */
    public void setFail(Integer fail) {
        if (fail == null || fail <= 0) {
            this.fail = null;
        } else {
            this.fail = fail;
        }
    }

    /**
     * Get the fail value
     * @return the fail value
     */
    public Integer getFail() {
        /*
        if (fail == null) { // OLD CONFIG
            fail = DEFAULT_FAIL;
        }
        */
        return fail;
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
     * Set the usePattern value
     * @param usePattern the value to use.
     */
    public void setUsePattern(boolean usePattern) {
        this.usePattern = usePattern;
    }

    /**
     * Get the usePattern value.
     * @return the usePattern value.
     */
    public boolean isUsePattern() {
        return usePattern;
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

    private static final long serialVersionUID = 1L;
}
