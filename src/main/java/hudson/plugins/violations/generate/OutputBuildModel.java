package hudson.plugins.violations.generate;

import java.io.PrintWriter;
import java.io.IOException;

import java.util.Set;
import java.util.SortedSet;
import java.util.HashMap;
import java.util.Map;

import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.model.FileSummary;
import hudson.plugins.violations.model.Severity;

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
                           + "'>");
            doSeverities(type, f.getFileModel());
            writer.println("  </file>");
        }
        writer.println("  </type>");
    }

    /** Output the severity numbers for a particular type in a file */
    private void doSeverities(String type, FullFileModel file) {
        Set<Violation> violations = file.getTypeMap().get(type);
        if (violations == null) {
            return;
        }
        // Build up the severity counts and code numbers
        CodeCountMap codeMap = new CodeCountMap();
        int[] counts = new int[Severity.NUMBER_SEVERITIES];
        for (Violation v: violations) {
            counts[v.getSeverityLevel()]++;
            codeMap.add(v.getSource());
        }
        // Output severity counts
        for (int i = 0; i < counts.length; ++i) {
            if (counts[i] == 0) {
                continue;
            }
            writer.print(
                "      <severity");
            writer.print(XMLUtil.toAttribute("level", i));
            writer.print(XMLUtil.toAttribute("count", counts[i]));
            writer.println("/>");
        }
        // Output severity code counts
        for (Map.Entry<String, Integer> e: codeMap.entrySet()) {
            writer.print("      <source ");
            writer.print(XMLUtil.toAttribute("name", e.getKey()));
            writer.print(XMLUtil.toAttribute("count", e.getValue()));
            writer.println("/>");
        }
    }

    private static class CodeCountMap
        extends HashMap<String, Integer> {
        public void add(String code) {
            Integer k = super.get(code);
            if (k == null) {
                k = 0;
            }
            int i = k;
            i++;
            super.put(code, i);
        }
    }

    private void footer() throws IOException {
        writer.println("</violations>");
    }
}
