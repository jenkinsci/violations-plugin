package hudson.plugins.violations.types.pylint;

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
 * Parser for parsing PyLint reports.
 *
 * The parser only supports PyLint report that has the output format
 * 'parseable'.
 *
 * @author Erik Ramfelt
 */
public class PyLintParser implements ViolationsParser {

    /** Regex pattern for the PyLint errors. */
    private final transient Pattern pattern;
    private transient AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder(); 

    /**
     * Constructor - create the pattern.
     */
    public PyLintParser() {
        pattern = Pattern.compile("(.*):(\\d+): \\[(\\D\\d*).*\\] (.*)");
    }

    /** {@inheritDoc} */
    public void parse( FullBuildModel model, File projectPath, String fileName,
        String[] sourcePaths) throws IOException {
        
    	BufferedReader reader = null;
        
    	absoluteFileFinder.addSourcePath(projectPath.getAbsolutePath());
    	absoluteFileFinder.addSourcePaths(sourcePaths);
        
        try {
            reader = new BufferedReader(
                new FileReader(new File(projectPath, fileName)));
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
     * Parses a PyLint line and adding a violation if regex
     * @param model build model to add violations to
     * @param line the line in the file.
     * @param projectPath the path to use to resolve the file.
     */
    public void parseLine(FullBuildModel model, String line, File projectPath) {
        PyLintViolation pyLintViolation = getPyLintViolation(line);

        if (pyLintViolation != null) {

            Violation violation = new Violation();
            violation.setType("pylint");
            violation.setLine(pyLintViolation.getLineStr());
            violation.setMessage(pyLintViolation.getMessage());
            violation.setSource(pyLintViolation.getViolationId());
            setServerityLevel(violation, pyLintViolation.getViolationId());

            FullFileModel fileModel = getFileModel(model, 
            		pyLintViolation.getFileName(), 
            		absoluteFileFinder.getFileForName(pyLintViolation.getFileName()));
            fileModel.addViolation(violation);
        }
    }
    
    private FullFileModel getFileModel(FullBuildModel model, String name, File sourceFile) {
        FullFileModel fileModel = model.getFileModel(name);
        File other = fileModel.getSourceFile();

        if (sourceFile == null
            || ((other != null) && (
                    other.equals(sourceFile)
                    || other.exists()))) {
            return fileModel;
        }
        
        fileModel.setSourceFile(sourceFile);
        fileModel.setLastModified(sourceFile.lastModified());
        return fileModel;
    }
    

    /**
     * Returns a pylint violation (if it is one)
     * @param line a line from the PyLint parseable report
     * @return a PyLintViolation if the line contains one; null otherwise
     */
    PyLintViolation getPyLintViolation(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == 4) {
            return new PyLintViolation(matcher);
        }
        return null;
    }

    /**
     * Returns the Severity level as an int from the PyLint message type.
     *
     * The different message types are:
     * (C) convention, for programming standard violation
     * (R) refactor, for bad code smell
     * (W) warning, for python specific problems
     * (E) error, for much probably bugs in the code
     * (F) fatal, if an error occured which prevented pylint from doing
     *     further processing.
     *
     * @param messageType the type of PyLint message
     * @return an int is matched to the message type.
     */
    private void setServerityLevel(Violation violation, String messageType) {

        switch (messageType.charAt(0)) {
            case 'C':
                violation.setSeverity(Severity.LOW);
                violation.setSeverityLevel(Severity.LOW_VALUE);
                break;
            case 'R':
                violation.setSeverity(Severity.MEDIUM_LOW);
                violation.setSeverityLevel(Severity.MEDIUM_LOW_VALUE);
                break;
            case 'W':
            default:
                violation.setSeverity(Severity.MEDIUM);
                violation.setSeverityLevel(Severity.MEDIUM_VALUE);
                break;
            case 'E':
            case 'F':
                violation.setSeverity(Severity.HIGH);
                violation.setSeverityLevel(Severity.HIGH_VALUE);
                break;
        }
    }
    
    class PyLintViolation {
        private final transient String lineStr;
        private final transient String message;
        private final transient String fileName;
        private final transient String violationId;

        public PyLintViolation(Matcher matcher) {
            if (matcher.groupCount() < 4) {
                throw new IllegalArgumentException(
                    "The Regex matcher could not find enough information");
            }
            lineStr = matcher.group(2);
            message = matcher.group(4);
            violationId = matcher.group(3);
            fileName = matcher.group(1);
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
