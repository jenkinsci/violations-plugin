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
    
    /**
     * Returns a relative path from the self to the other path.
     * Note that the self and other paths must have '/' as the separator.
     * @param self the path to get a relative path from.
     * @param other the path to get a relative path to. 
     * @return a string containing a relative path from self to other path.
     */
    public static String relativePath(final String self, final String other) {
        if (self.equals(other)) {
            return "";
        }

        String[] selfParts = self.split("/");
        String[] otherParts = other.split("/");
        int len = selfParts.length;
        if (otherParts.length < len) {
            len = otherParts.length;
        }

        int same = 0; // used outside for loop
        for (; same < len; same++) {
            if (!selfParts[same].equals(otherParts[same])) {
                break;
            }
        }
        StringBuilder b = new StringBuilder();
        for (int i = same + 1; i < selfParts.length; ++i) {
            b.append("../");
        }
        boolean first = true;
        for (int i = same; i < otherParts.length; ++i) {
            if (!first) {
                b.append("/");
            }
            first = false;
            b.append(otherParts[i]);
        }
        return b.toString();
    }
}
