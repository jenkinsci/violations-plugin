package hudson.plugins.violations.generate;

import hudson.FilePath;
import hudson.plugins.violations.util.CloseUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * A controller class to execute a xml parser object.
 */
public class ExecuteFilePath {
    private final FilePath targetDir;
    private final String filename;
    private final Execute ex;

    /**
     * Create the object.
     * @param targetDir the directory used for resolving filenames.
     * @param filename  the file to parse.
     * @param ex        the parser object.
     */
    public ExecuteFilePath(FilePath targetDir, String filename, Execute ex) {
        this.targetDir = targetDir;
        this.filename = filename;
        this.ex = ex;
    }

    /**
     * Parse the file.
     * @throws IOException if there is a problem.
     * @throws InterruptedException if interrupted.
     */
    public void execute() throws IOException, InterruptedException {
        boolean seenException = false;
        FilePath f = targetDir.child(filename);
        PrintWriter w = new PrintWriter(
            new BufferedWriter(
                new OutputStreamWriter(
                    f.write(), "UTF-8"))); // ? for html //
        try {
            ex.execute(w);
        } catch (IOException ex) {
            seenException = true;
            throw ex;
        } finally {
            CloseUtil.close(w, seenException);
        }
    }
}
