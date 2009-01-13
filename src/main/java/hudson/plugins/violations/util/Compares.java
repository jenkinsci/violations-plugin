package hudson.plugins.violations.util;

/**
 * A utility class to help compare objects.
 */
public class Compares {
    /**
     * Compare two (nullable) objects.
     * @param a the first object
     * @param b the second object
     * @return 0 if the objects are the same,
     *     -1 if first is less than second or is null,
     *     1 if second is less that first or is null.
     */
    public static int compare(Comparable a, Comparable  b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        return a.compareTo(b);
    }

    /**
     * Compare a list of pair of objects.
     * @return 0 if all pairs are the same, else the result of the first
     *           pair that is not the same.
     */
    public static int compare(Comparable... objects) {
        if (objects.length < 2) {
            return 0;
        }
        for (int i = 0; i < objects.length; i += 2) {
            Comparable o1 = objects[i];
            Comparable o2 = objects[i + 1];
            int result = compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

}
