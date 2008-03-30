package hudson.plugins.violations.generate;

import java.io.PrintWriter;
import java.io.IOException;

/**
 * Interface for classes to output. This is used
 * by ExecuteFilePath.
 */
public interface Execute {
    /**
     * Execute the class with a particular print writer.
     * @param p the printwriter.
     * @throws IOException if there is a problem writing.
     */
    void execute(PrintWriter p) throws IOException;
}
