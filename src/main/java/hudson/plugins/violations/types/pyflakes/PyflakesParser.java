package hudson.plugins.violations.types.pyflakes;

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
 * Parser for parsing Pyflakes reports.
 *
 * The parser supports Pyflakes report generated like this:
 *
 * <pre>
 * pyflakes input.py
 * </pre>
 *
 * @author Erik Ramfelt
 */
public class PyflakesParser implements ViolationsParser {

    /** Regex pattern for the Pyflakes errors. */
    private final transient Pattern pattern;
    private transient AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder();

    /**
     * Constructor - create the pattern.
     */
    public PyflakesParser() {
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
     * Parses a Pyflakes line and adding a violation if regex
     *
     * @param model
     *            build model to add violations to
     * @param line
     *            the line in the file.
     * @param projectPath
     *            the path to use to resolve the file.
     */
    public void parseLine(FullBuildModel model, String line, File projectPath) {
        PyflakesViolation pyFlakesViolation = getPyflakesViolation(line);

        if (pyFlakesViolation != null) {

            Violation violation = new Violation();
            violation.setType("pyflakes");
            violation.setLine(pyFlakesViolation.getLineStr());
            violation.setMessage(pyFlakesViolation.getMessage());
            violation.setSource(pyFlakesViolation.getViolationId());
            setServerityLevel(violation, pyFlakesViolation.getViolationId());

            FullFileModel fileModel = getFileModel(model,
                    pyFlakesViolation.getFileName(),
                    absoluteFileFinder.getFileForName(pyFlakesViolation
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
     * Returns a pyflakes violation (if it is one)
     *
     * @param line
     *            a line from the Pyflakes parseable report
     * @return a PyflakesViolation if the line contains one; null otherwise
     */
    PyflakesViolation getPyflakesViolation(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == 3) {
            return new PyflakesViolation(matcher);
        }
        return null;
    }

    /**
     * Returns the Severity level as an int from the Pyflakes message type.
     *
     * <pre>
     * The different message types are:
     * (C) convention, for programming standard violation
     * (R) refactor, for bad code smell
     * (W) warning, for python specific problems
     * (E) error, for much probably bugs in the code
     * (F) fatal, if an error occured which prevented pyflakes from doing
     *     further processing.
     * </pre>
     *
     * @param messageType
     *            the type of Pyflakes message
     * @return an int is matched to the message type.
     */
    private void setServerityLevel(Violation violation, String messageType) {
        violation.setSeverity(Severity.HIGH);
        violation.setSeverityLevel(Severity.HIGH_VALUE);
    }

    class PyflakesViolation {
        private final transient String lineStr;
        private final transient String message;
        private final transient String fileName;
        private final transient String violationId;

        public PyflakesViolation(Matcher matcher) {
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
