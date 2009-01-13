package hudson.plugins.violations.util;

/**
 * A utility class to help see if objects are the same.
 */
public class Equals {
    /**
     * Compare a list of objects.
     * @return true if all the objects are the same.
     */
    public static boolean equals(Object... objects) {
        if (objects.length < 2) {
            return true;
        }
        for (int i = 0; i < objects.length - 1; ++i) {
            Object o1 = objects[i];
            Object o2 = objects[i + 1];
            if (!equals(o1, o2)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Compare two objects.
     * @return true if the two objects are the same.
     */
    public static boolean equals(Object a, Object b) {
        if (a == null) {
            return b == null;
        }
        return a.equals(b);
    }
}
