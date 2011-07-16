package hudson.plugins.violations.util;

/**
 * A class to generate a health number for
 * a value based on min and max.
 */
public class HealthNumber {

    private static final int SUNNY_TOP = 100;
    private static final int SUNNY_SPAN = 19;
    private static final int INTER_TOP = 79;
    private static final int INTER_SPAN = 58;
    private static final int STORMY_SPAN = 19;

    private final int min;
    private final int max;

    /**
     * Constructor the class.
     * @param min the min (sunny) value.
     * @param max the max (stormy) value.
     */
    public HealthNumber(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * calculate the number.
     * @param val the input value.
     * @return the health number;
     */
    public int calculate(int val) {
        int ret = 0;
        if (val <= min) {
            // return a number between 100 and 81 (sunny)
            if (min == 0) {
                ret = SUNNY_TOP;
            } else {
                ret = SUNNY_TOP - (val * SUNNY_SPAN) / min;
            }
        } else if (val < max) {
            // return a number between 79 and 21
            ret = INTER_TOP - ((val - min) * INTER_SPAN / (max - min));
        } else {
            //  return 0 to 19 (stormy)
            ret = STORMY_SPAN / val;
        }
        return ret;
    }
}

