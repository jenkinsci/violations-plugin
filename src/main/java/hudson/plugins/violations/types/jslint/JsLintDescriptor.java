package hudson.plugins.violations.types.jslint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The descriptor class for JSLint (checkstyle for JavaScript) violations type.
 *
 * @author cliffano
 */
public class JsLintDescriptor extends TypeDescriptor {

    public static final String JSLINT = "jslint";

    public JsLintDescriptor() {
        super(JSLINT);
    }

    /**
     * Create a parser for the JSLint type.
     *
     * @return a new JSLint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new JsLintParser();
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
        ret.add("jslint.xml");
        return ret;
    }
}