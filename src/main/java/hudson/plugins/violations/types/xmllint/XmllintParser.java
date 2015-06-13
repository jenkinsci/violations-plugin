package hudson.plugins.violations.types.xmllint;

import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.util.AbsoluteFileFinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for parsing Xmllint reports.
 *
 * The parser only supports Xmllint report that has the output format
 * 'parseable'.
 *
 * @author Erik Ramfelt
 */
public class XmllintParser implements ViolationsParser {

    /** Regex pattern for the Xmllint errors. */
    private final transient Pattern pattern;
    private transient AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder();

    /**
     * Constructor - create the pattern.
     */
    public XmllintParser() {
        pattern = Pattern.compile("(.*):(\\d+): (.*)");
    }

    /** {@inheritDoc} */
    public void parse(FullBuildModel model, File projectPath, String fileName,
            String[] sourcePaths) throws IOException {

        BufferedReader reader = null;

        absoluteFileFinder.addSourcePath(projectPath.getAbsolutePath());
        absoluteFileFinder.addSourcePaths(sourcePaths);

        try {
            reader = new BufferedReader(new FileReader(new File(projectPath,
                    fileName)));
            String line = reader.readLine();
            while (line != null) {
                parseLine(model, line, projectPath);
                line = reader.readLine();
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Parses a Xmllint line and adding a violation if regex
     *
     * @param model
     *            build model to add violations to
     * @param line
     *            the line in the file.
     * @param projectPath
     *            the path to use to resolve the file.
     */
    public void parseLine(FullBuildModel model, String line, File projectPath) {
        XmllintViolation xmllintViolation = getXmllintViolation(line);

        if (xmllintViolation != null) {

            Violation violation = new Violation();
            violation.setType("xmllint");
            violation.setLine(xmllintViolation.getLineStr());
            violation.setMessage(xmllintViolation.getMessage());
            violation.setSource(xmllintViolation.getViolationId());
            setServerityLevel(violation, xmllintViolation.getViolationId());

            FullFileModel fileModel = getFileModel(model,
                    xmllintViolation.getFileName(),
                    absoluteFileFinder.getFileForName(xmllintViolation
                            .getFileName()));
            fileModel.addViolation(violation);
        }
    }

    private FullFileModel getFileModel(FullBuildModel model, String name,
            File sourceFile) {
        FullFileModel fileModel = model.getFileModel(name);
        File other = fileModel.getSourceFile();

        if (sourceFile == null
                || ((other != null) && (other.equals(sourceFile) || other
                        .exists()))) {
            return fileModel;
        }

        fileModel.setSourceFile(sourceFile);
        fileModel.setLastModified(sourceFile.lastModified());
        return fileModel;
    }

    /**
     * Returns a xmllint violation (if it is one)
     *
     * @param line
     *            a line from the Xmllint parseable report
     * @return a XmllintViolation if the line contains one; null otherwise
     */
    XmllintViolation getXmllintViolation(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == 3) {
            return new XmllintViolation(matcher);
        }
        return null;
    }

    /**
     * Returns the Severity level as an int from the Xmllint message type.
     *
     * The different message types are:
     *
     * <pre>
     * (C) convention, for programming standard violation
     * (R) refactor, for bad code smell
     * (W) warning, for python specific problems
     * (E) error, for much probably bugs in the code
     * (F) fatal, if an error occured which prevented xmllint from doing
     *     further processing.
     * </pre>
     *
     * @param messageType
     *            the type of Xmllint message
     * @return an int is matched to the message type.
     */
    private void setServerityLevel(Violation violation, String messageType) {
        violation.setSeverity(Severity.HIGH);
        violation.setSeverityLevel(Severity.HIGH_VALUE);
    }

    class XmllintViolation {
        private final transient String lineStr;
        private final transient String message;
        private final transient String fileName;
        private final transient String violationId;

        public XmllintViolation(Matcher matcher) {
            if (matcher.groupCount() < 3) {
                throw new IllegalArgumentException(
                        "The Regex matcher could not find enough information");
            }
            lineStr = matcher.group(2);
            message = matcher.group(3);
            fileName = matcher.group(1);
            violationId = "C";
        }

        public String getLineStr() {
            return lineStr;
        }

        public String getMessage() {
            return message;
        }

        public String getFileName() {
            return fileName;
        }

        public String getViolationId() {
            return violationId;
        }

    }
}
