package hudson.plugins.violations.types.cpplint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.util.AbsoluteFileFinder;

/**
 * Parser for parsing CppLint reports.
 *
 * @author Jos Houtman
 */
public class CppLintParser implements ViolationsParser {

    /** Regex pattern for the CppLint errors. */
    private final transient Pattern pattern;
    private transient AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder(); 

    /**
     * Constructor - create the pattern.
     */
    public CppLintParser() {
        pattern = Pattern.compile("(.*):(\\d+): (.*) \\[(.*)\\] \\[(.*)\\]");
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
     * Parses a CppLint line and adding a violation if regex
     * @param model build model to add violations to
     * @param line the line in the file.
     * @param projectPath the path to use to resolve the file.
     */
    public void parseLine(FullBuildModel model, String line, File projectPath) {
        CppLintViolation cppLintViolation = getCppLintViolation(line);

        if (cppLintViolation != null) {

            Violation violation = new Violation();
            violation.setType("cpplint");
            violation.setLine(cppLintViolation.getLineStr());
            violation.setMessage(cppLintViolation.getMessage());
            violation.setSource(cppLintViolation.getViolationId());
            setServerityLevel(violation, cppLintViolation.getConfidence());

            FullFileModel fileModel = getFileModel(model, 
            		cppLintViolation.getFileName(), 
            		absoluteFileFinder.getFileForName(cppLintViolation.getFileName()));
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
     * Returns a cpplint violation (if it is one)
     * @param line a line from the cppLint parseable report
     * @return a CppLintViolation if the line contains one; null otherwise
     */
    CppLintViolation getCppLintViolation(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == 5) {
            return new CppLintViolation(matcher);
        }
        return null;
    }

    /**
     * Returns the Severity level as an int from the Cpplint confidence message type.
     *
     * The different message types are 1 through 5, indicating the confidence.
     *
     * @param messageType the type of CppLint message
     * @return an int is matched to the message type.
     */
    private void setServerityLevel(Violation violation, String confidenceType) {

        switch (confidenceType.charAt(0)) {
            case '1':
	    case '2':
                violation.setSeverity(Severity.LOW);
                violation.setSeverityLevel(Severity.LOW_VALUE);
                break;
            case '3':
	    case '4':
	    default:
                violation.setSeverity(Severity.MEDIUM_LOW);
                violation.setSeverityLevel(Severity.MEDIUM_LOW_VALUE);
                break;
            case '5':
                violation.setSeverity(Severity.HIGH);
                violation.setSeverityLevel(Severity.HIGH_VALUE);
                break;
        }
    }
    
    class CppLintViolation {
        private final transient String lineStr;
        private final transient String message;
        private final transient String fileName;
        private final transient String violationId;
	private final transient String confidence;

        public CppLintViolation(Matcher matcher) {
            if (matcher.groupCount() < 5) {
                throw new IllegalArgumentException(
                    "The Regex matcher could not find enough information");
            }
            lineStr = matcher.group(2);
            message = matcher.group(3);
            violationId = matcher.group(4);
            confidence = matcher.group(5);
            fileName = matcher.group(1);
        }

        public String getLineStr() {
            return lineStr;
        }

        public String getMessage() {
            return message + " (" + confidence + ")";
        }

        public String getFileName() {
            return fileName;
        }

        public String getViolationId() {
            return violationId;
        }

	public String getConfidence() {
	    return confidence;
	}
    }
}
