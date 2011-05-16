package hudson.plugins.violations.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class to close closables.
 */
public final class CloseUtil {
    private static final Logger LOG = Logger.getLogger(
        CloseUtil.class.getName());

    /** private contstructor */
    private CloseUtil() {
    }

    /**
     * Close a closable - optional ignoring exceptions.
     * @param closable the closable to close.
     * @param ignore   if true do not throw an exception if there
     *                 is one.
     * @throws IOException if there is a problem closing the closable.
     */
    public static void close(Closeable closable, boolean ignore)
        throws IOException {
        if (closable == null) {
            return;
        }
        try {
            closable.close();
        } catch (IOException ex) {
            if (!ignore) {
                throw ex;
            } else {
                LOG.log(
                    Level.INFO, "Ignoring exception closing " + closable, ex);
            }
        }
    }

    /**
     * Close a closable ignoring exceptions.
     * @param closable the closable to close.
     */
    public static void close(Closeable closable) {
        try {
            close(closable, true);
        } catch (IOException ex) {
            LOG.log(Level.INFO, "close() Should not happen!", ex);
        }
    }
}
