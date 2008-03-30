package hudson.plugins.violations.util;

/**
 * A utility class for strings.
 */
public final class StringUtil {

    /** private contstructor */
    private StringUtil() {
    }

    /**
     * Check if the string is null or all spaces.
     * @param str the string to test.
     * @return true if string is null or all spaces.
     */
    public static boolean isBlank(String str) {
        return str == null ? true : "".equals(str.trim());
    }
}
