package hudson.plugins.violations.generate;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.Set;
import java.util.Map;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import hudson.plugins.violations.util.CloseUtil;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.ViolationsConfig;

/**
 * Class to output a file model.
 */
public class OutputFileModel implements Execute {
    private static final int NEAR = 10;

    private static class LimitedType {
        private int number = 0;
        private Set<Violation> violations = new TreeSet<Violation>();
    }

    private final FullFileModel fileModel;
    private final ViolationsConfig config;
    private PrintWriter w;

    private BufferedReader sourceReader = null;

    private HashMap<Integer, Set<Violation>> vMap
        = new HashMap<Integer, Set<Violation>>();

    private SortedMap<String, LimitedType> limitedMap
        = new TreeMap<String, LimitedType>();

    /**
     * Create a class to output a file model.
     * @param fileModel the model to output.
     * @param config    the configuration - used to suppress outputing
     *                  violations.
     */
    public OutputFileModel(FullFileModel fileModel,
                           ViolationsConfig config) {
        this.fileModel = fileModel;
        this.config = config;
    }

    /**
     * Output the file model.
     * @param w the output writer.
     * @throws IOException if there is a problem.
     */
    public void execute(PrintWriter w) throws IOException {
        this.w = w;
        w.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        w.println("<file");
        w.print(" ");
        w.println(XMLUtil.toAttribute("name", fileModel.getDisplayName()));
        w.print(" ");
        w.println(XMLUtil.toAttribute(
                      "last-modified",
                      "" + fileModel.getLastModified()));
        if (fileModel.getSourceFile() != null) {
            w.print(" ");
            w.println(XMLUtil.toAttribute(
                          "file", fileModel.getSourceFile().getAbsolutePath()));
        }
        w.println(">");
        createLimited();
        if (fileModel.getSourceFile() != null
            && fileModel.getSourceFile().exists()) {
            try {
                outputContents();
            } finally {
                closeSourceFile();
            }
        }

        outputSummary();

        w.println("</file>");
    }

    private void addToVMap(Violation v) {
        if (vMap.get(v.getLine()) == null) {
            vMap.put(v.getLine(), new TreeSet<Violation>());
        }
        vMap.get(v.getLine()).add(v);
    }

    private void createLimited() throws IOException {
        for (Map.Entry<String, TreeSet<Violation>> e
                 : fileModel.getTypeMap().entrySet()) {
            String type = e.getKey();
            Set<Violation> violations = e.getValue();
            LimitedType limitedType = new LimitedType();
            limitedType.number = violations.size();
            int c = 0;
            for (Violation v: violations) {
                limitedType.violations.add(v);
                addToVMap(v);
                doViolation(v);
                c++;
                if (c >= config.getLimit()) {
                    break;
                }
            }
            limitedMap.put(type, limitedType);
        }
    }

    private void closeSourceFile() {
        CloseUtil.close(sourceReader);
        sourceReader = null;
    }


    private boolean near(int lineNumber) {
        for (int i = lineNumber - NEAR; i < lineNumber + NEAR; ++i) {
            if (vMap.get(i) != null) {
                return true;
            }
        }
        return false;
    }

    private void outputSummary() throws IOException {
        for (Map.Entry<String, LimitedType> e: limitedMap.entrySet()) {
            String type = e.getKey();
            LimitedType t = e.getValue();
            w.println("  <type type='" + type + "' number='" + t.number
                      + "' suppressed='"
                      + ((t.number <= config.getLimit()) ? 0
                         : t.number - config.getLimit())
                      + "'/>");
        }
    }

    private void outputContents() throws IOException {
        sourceReader = new BufferedReader(
            new FileReader(fileModel.getSourceFile()));
        int lineNumber = 1;
        while (true) {
            String line = sourceReader.readLine();
            if (line == null) {
                break;
            }
            if (!near(lineNumber)) {
                lineNumber++;
                continue;
            }
            w.print("  <line number='" + lineNumber + "'>");
            w.print(XMLUtil.escapeContent(line));
            w.println("</line>");
            lineNumber++;
        }
    }

    private void printAttr(String name, Object value)
        throws IOException {
        if (value == null) {
            return;
        }
        w.print  ("   ");
        w.println(XMLUtil.toAttribute(name, value.toString()));
    }

    private void doViolation(Violation v)
        throws IOException {
        w.println("  <violation");
        printAttr("line", v.getLine());
        printAttr("source", v.getSource());
        printAttr("severity", v.getSeverity());
        printAttr("type", v.getType());
        printAttr("message", v.getMessage().trim());
        printAttr("severity-level", v.getSeverityLevel());
        if (v.getPopupMessage() != null) {
            printAttr("popup-message", v.getPopupMessage().trim());
        }
        w.println("  />");
    }
}
