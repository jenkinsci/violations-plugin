package hudson.plugins.violations.util;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * A helper class to recuruse down a uri path
 * to an object.
 * I do not know how to do this any other way.
 */
public class RecurDynamic {
    private final String pathSoFar;
    private final String pathToReach;
    private final Object endObject;

    /**
     * Create a RecurDynamic helper object.
     * @param pathSoFar the path reached up to this object.
     * @param pathToReach the path that is being parsed.
     * @param endObject  the object to return for the last token of the path.
     */
    public RecurDynamic(
        String pathSoFar, String pathToReach, Object endObject) {
        this.pathSoFar = pathSoFar;
        this.pathToReach = pathToReach;
        this.endObject = endObject;
    }

    /**
     * Get the next object for the path.
     * @param token the current token.
     * @param req   the http/stapler request object.
     * @param rsp   the http/stapler response object.
     * @return the end object if the token is the last token,
     *         another RecurDynamic if not at rhe end of the path,
     *         null if the path does not match.
     */
    public Object getDynamic(
        final String token, StaplerRequest req, StaplerResponse rsp) {
        final String testPath = pathSoFar + token;
        if (pathToReach.equals(testPath)) {
            return endObject;
        } else if (pathToReach.startsWith(testPath)) {
            return new RecurDynamic(testPath + "/", pathToReach, endObject);
        } else {
            return null;
        }
    }
}


