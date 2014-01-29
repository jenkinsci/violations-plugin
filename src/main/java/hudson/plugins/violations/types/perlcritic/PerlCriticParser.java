package hudson.plugins.violations.types.perlcritic;

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
 * Parser for parsing Perl::Critic reports.
 *
 * The parser supports stock Perl::Critic output, without output-modifying 
 * option flags (e.g.: `perlcritic --brutal .`).
 *
 * Also adapted from the pylint parser
 *
 * @author David McGuire
 */
public class PerlCriticParser implements ViolationsParser {

    /** Regex pattern for the Perl::Critic errors, matching the output form:
     * [filename]: [violation] at line [line number], column [column number] [PBP reference] (Severity: [severity level 1-5])
     * 
     * To ensure that this format is used, use the verbosity flag with a value of 5
     * (e.g.: `perlcritic --verbose 5 .`)
     * this overwrites any use of Perl::Critic resource files (e.g.: .perlcriticrc)
     * 
     * Validated with Eclipse EPIC plug-in RegExp tool against the output from 
     * `perlcritic --brutal /usr/local/share/perl/5.10.1/Perl/Critic/`
     */
    private final static String regex = "^(.*?): (.*) at line (\\d+), column (\\d+).\\s*(.*)\\.\\s*\\(Severity: (\\d)\\)$";
    private final transient int groups = 6;  /** Number of match groups expected (SEE regex) */
    private final transient Pattern pattern; /** Compiled pattern */
    private transient AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder(); 

    /**
     * Constructor - compile the regex to a Pattern.
     */
    public PerlCriticParser() {
        this.pattern = Pattern.compile(PerlCriticParser.regex);
    }

    /** {@inheritDoc} */
    public void parse( FullBuildModel model, File projectPath, String fileName, String[] sourcePaths) throws IOException {

        BufferedReader reader = null;

        absoluteFileFinder.addSourcePath(projectPath.getAbsolutePath());
        absoluteFileFinder.addSourcePaths(sourcePaths);

        try {
            reader = new BufferedReader(new FileReader(new File(projectPath, fileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                this.parseLine(model, line, projectPath);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Parses a Perl::Critic line and adds a Violation if this.pattern matches
     * @param model build model to which violations should be added
     * @param line the line in the source file on which violation occurred
     * @param projectPath the project path to use to resolve the source file
     */
    public void parseLine(FullBuildModel model, String line, File projectPath) {
        PerlCriticViolation perlCriticViolation = getPerlCriticViolation(line);

        if (perlCriticViolation == null) {
            return;
        }

        String fileName = perlCriticViolation.getFileName();
        FullFileModel fileModel = this.getFileModel(model, fileName);
        fileModel.addViolation(perlCriticViolation);
    }
    
    private FullFileModel getFileModel(FullBuildModel model, String name) {
        File sourceFile = this.absoluteFileFinder.getFileForName(name);
        FullFileModel fileModel = model.getFileModel(name);
        File other = fileModel.getSourceFile();

        if (sourceFile == null
            || ((other != null) 
                && (other.equals(sourceFile)
                    || other.exists()))) {
            return fileModel;
        }

        fileModel.setSourceFile(sourceFile);
        fileModel.setLastModified(sourceFile.lastModified());
        return fileModel;
    }
    

    /**
     * Create a Perl::Critic Violation (if the line is a match)
     * @param line a line from the Perl::Critic report
     * @return a PerlCriticViolation if the line matches; null otherwise
     */
    PerlCriticViolation getPerlCriticViolation(String line) {
        Matcher matcher = pattern.matcher(line);

        if (!(matcher.find() && matcher.groupCount() == this.groups)) {
            return null;
        }

        return new PerlCriticViolation(matcher);
    }

    class PerlCriticViolation extends Violation {
        private int column;
        private String fileName;
        private static final int fields = 6; 

        public PerlCriticViolation(Matcher matcher) {

            if (matcher.groupCount() < PerlCriticViolation.fields) {
                throw new IllegalArgumentException(
                    "The Regex matcher could not find enough information");
            }

            this.fileName = matcher.group(1);
            this.setMessage(matcher.group(2));
            this.setLine(matcher.group(3));
            this.setColumn(matcher.group(4));
            this.setSource(matcher.group(5));
            this.setViolationSeverity(matcher.group(6));
            this.setType(PerlCriticDescriptor.TYPE_NAME);
        }

        private void setColumn(String column) {
            try {
                int c = Integer.parseInt(column);
                this.setColumn(c);
            } catch (Exception ex) {
                this.setColumn(-1);
            }
        }

        private void setColumn(int column) {
            this.column = column;
        }

        public int getColumn() {
            return column;
        }

        public String getFileName() {
            return fileName;
        }

        /**
         * Sets the base Violation class's severity level from the Perl::Critic severity Level.
         *
         * Perl::Critic severity levels are integer values 1-5, with 5 being most severe.
         *
         * @param perlCriticSeverity severity level from the Perl::Critic output line
         */
        public void setViolationSeverity(int perlCriticSeverity) {
            int violationSeverity = 5 - perlCriticSeverity; // [1-5] --> [4-0]
            this.setSeverityLevel(violationSeverity);
            this.setSeverity(Severity.NAMES[violationSeverity]);
        }

        /**
         * Sets the severity as a String, if it parses to an integer,
         * and to the maximum severity, otherwise.
         * 
         * @see setViolationSeverity(int perlCriticSeverity)
         */
        public void setViolationSeverity(String perlCriticSeverity) {
            try {
                int severity = Integer.parseInt(perlCriticSeverity);
                this.setViolationSeverity(severity);
            } catch (Exception ex) {
                this.setViolationSeverity(5); // assume the worst
            }
        }
    }
}
