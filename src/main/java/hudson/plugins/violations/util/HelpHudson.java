package hudson.plugins.violations.util;

import org.kohsuke.stapler.StaplerRequest;

/**
 * Some utility methds for hudson.
 */
public final class HelpHudson {

    private static final int BUILD_NUMBER_POS = 3;

    /** Private construcor. */
    private HelpHudson() {
    }

    /**
     * get the build number from a uri.
     * @param req the request params
     * @return the build numberr if presend, 0 ofherwise.
     */
    public static int findBuildNumber(StaplerRequest req) {
        String requestURI = req.getOriginalRequestURI();
        String contextPath = req.getContextPath();
        if (contextPath.length() != 0) {
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
