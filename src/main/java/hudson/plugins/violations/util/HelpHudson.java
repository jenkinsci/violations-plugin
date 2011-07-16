package hudson.plugins.violations.util;

import org.kohsuke.stapler.StaplerRequest;

/**
 * Some utility methods for hudson.
 */
public final class HelpHudson {

    private static final int BUILD_NUMBER_POS = 3;

    /** Private constructor. */
    private HelpHudson() {
    }

    /**
     * get the build number from a uri.
     * @param req the request params
     * @return the build number if present, 0 otherwise.
     */
    public static int findBuildNumber(StaplerRequest req) {
        String requestURI = req.getOriginalRequestURI();
        String contextPath = req.getContextPath();
        if (contextPath != "") {
            if (!requestURI.startsWith(contextPath)) {
                return 0;
            }
            requestURI = requestURI.substring(contextPath.length());
        }
        // check if starts with /job
        if (!requestURI.startsWith("/job")) {
            return 0;
        }
        String[] parts = requestURI.split("/");
        // blank + job + jobname + number + rest...
        if (parts.length < (BUILD_NUMBER_POS + 1)) {
            return 0;
        }
        try {
            return Integer.parseInt(parts[BUILD_NUMBER_POS]);
        } catch (Exception ex) {
            return 0;
        }
    }
}
