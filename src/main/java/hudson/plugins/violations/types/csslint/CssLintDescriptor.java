package hudson.plugins.violations.types.csslint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The descriptor class for JSLint (checkstyle for CSS) violations type.
 * 
 * @author cliffano, mbrunken
 *
 */
public class CssLintDescriptor extends TypeDescriptor {

    /** The descriptor for the JSLint violations type. */
    public static final CssLintDescriptor DESCRIPTOR = addDescriptor(new CssLintDescriptor());

    private CssLintDescriptor() {
        super("csslint");
    }

    /**
     * Create a parser for the JSLint type.
     * 
     * @return a new JSLint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new CssLintParser();
    }

    /**
     * TODO: check w/ jslint4java, what's the default output file name for Maven
     * build. Get a list of target xml files to look for for this particular
     * type.
     * 
     * @return a list filenames to look for in the target directory.
     */
    @Override
    public List<String> getMavenTargets() {
        List<String> ret = new ArrayList<String>();
        ret.add("csslint.xml");
        return ret;
    }
}