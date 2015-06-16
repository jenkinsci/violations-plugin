package hudson.plugins.violations.types.checkstyle;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.parse.AbstractTypeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The descriptor class for Checkstyle violations type.
 */
public final class CheckstyleDescriptor extends TypeDescriptor {

    public static final String CHECKSTYLE = "checkstyle";

    public CheckstyleDescriptor() {
        super(CHECKSTYLE);
    }

    /**
     * Create a parser for the checkstyle type.
     *
     * @return a new checkstyle parser.
     */
    @Override
    public AbstractTypeParser createParser() {
        return new CheckstyleParser();
    }

    /**
     * Get a list of target xml files to look for for this particular type.
     *
     * @return a list filenames to look for in the target target directory.
     */
    @Override
    public List<String> getMavenTargets() {
        List<String> ret = new ArrayList<String>();
        ret.add("checkstyle-result.xml");
        return ret;
    }
}
