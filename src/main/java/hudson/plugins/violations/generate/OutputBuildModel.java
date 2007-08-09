package hudson.plugins.violations.generate;

import java.io.PrintWriter;
import java.io.IOException;

import java.util.SortedSet;

import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FileSummary;

/**
 * Class to output the full build model (not the files).
 */
public class OutputBuildModel implements Execute {
    private final FullBuildModel model;
    private PrintWriter writer;

    /**
     * Create the output class.
     * @param model the model to output.
     */
    public OutputBuildModel(FullBuildModel model) {
        this.model = model;
    }

    /**
     * Output the build model.
     * @param writer the output writer.
     * @throws IOException if there is a problem.
     */
    public void execute(PrintWriter writer) throws IOException {
        this.writer = writer;
        header();
        for (String type: model.getTypeMap().keySet()) {
            doType(type, model.getTypeMap().get(type));
        }
        footer();
    }

    private void header() throws IOException {
        writer.println("<violations>");
    }

    private void doType(String type, SortedSet<FileSummary> files) {
        writer.println("  <type name='" + type + "'>");
        for (FileSummary f: files) {
            writer.println(
                "    <file");
            writer.println(
                "     "
                + XMLUtil.toAttribute(
                    "name",
                    f.getFileModel().getDisplayName()));
            writer.println("      count = '" + f.getViolations().size()
                           + "'/>");
        }
        writer.println("  </type>");
    }

    private void footer() throws IOException {
        writer.println("</violations>");
    }
}
