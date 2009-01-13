package hudson.plugins.violations.util;

/**
 * A utility class to help create hashcodes.
 */
public class HashCodes {
    /**
     * Create a hashcode from a list of objects.
     * @return the hashcode of the objects.
     */
    public static int hashCode(Object... objects) {
        int ret = 17;
        for (Object o: objects) {
            int c = o == null ? 0 : o.hashCode();
            ret = 31 * ret + c;
        }
        return ret;
    }
}
