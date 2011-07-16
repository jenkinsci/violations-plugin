package hudson.plugins.violations.types.pep8;

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
 * Parser for PEP 8 reports.
 * 
 * Supports only pep8 output where each line represents a
 * single issue report (i.e. no verbose, show-pep8 or
 * show-source arguments).
 * 
 * Adapted from the pylint parser.
 * 
 * @author Ali Rantakari
 */
public class Pep8Parser implements ViolationsParser {

    /** Regex pattern for the PEP 8 errors. */
    private final transient Pattern pattern;
    public static int MATCH_MIN_GROUP_COUNT = 4;
    private transient AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder(); 

    /**
     * Constructor - create the pattern.
     */
    public Pep8Parser() {
        pattern = Pattern.compile("(.*):(\\d+):\\d+: (\\D\\d*) (.*)");
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
     * Parses a PEP 8 line and adding a violation if regex
     * @param model build model to add violations to
     * @param line the line in the file.
     * @param projectPath the path to use to resolve the file.
     */
    public void parseLine(FullBuildModel model, String line, File projectPath) {
        Pep8Violation pep8Violation = getPep8Violation(line);

        if (pep8Violation != null) {

            Violation violation = new Violation();
            violation.setType("pep8");
            violation.setLine(pep8Violation.getLineStr());
            violation.setMessage(pep8Violation.getMessage());
            violation.setSource(pep8Violation.getViolationId());
            setServerityLevel(violation, pep8Violation.getViolationId());

            FullFileModel fileModel = getFileModel(model, 
            		pep8Violation.getFileName(), 
            		absoluteFileFinder.getFileForName(pep8Violation.getFileName()));
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
     * Returns a PEP 8 violation (if it is one)
     * @param line a line from the PEP 8 report
     * @return a Pep8Violation if the line contains one; null otherwise
     */
    Pep8Violation getPep8Violation(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == MATCH_MIN_GROUP_COUNT) {
            return new Pep8Violation(matcher);
        }
        return null;
    }

    /**
     * Returns the Severity level as an int from the PEP 8 message type.
     *
     * The different message types are W for warning and E for error.
     * Because these are style guide warnings we set the severity
     * values lower than what the apparent values would be.
     *
     * @param messageType the type of PEP 8 message
     * @return an int is matched to the message type.
     */
    private void setServerityLevel(Violation violation, String messageType) {

        switch (messageType.charAt(0)) {
            case 'E':
                violation.setSeverity(Severity.MEDIUM);
                violation.setSeverityLevel(Severity.MEDIUM_VALUE);
                break;
            case 'W':
            default:
                violation.setSeverity(Severity.LOW);
                violation.setSeverityLevel(Severity.LOW_VALUE);
                break;
        }
    }
    
    class Pep8Violation {
        private final transient String lineStr;
        private final transient String message;
        private final transient String fileName;
        private final transient String violationId;

        public Pep8Violation(Matcher matcher) {
            if (matcher.groupCount() < Pep8Parser.MATCH_MIN_GROUP_COUNT) {
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

