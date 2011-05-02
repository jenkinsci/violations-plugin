package hudson.plugins.violations.types.pmd;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.parse.AbstractTypeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The descriptor class for PMD violations type.
 */
public final class PMDDescriptor
    extends TypeDescriptor {

    /** The descriptor for the PMD violations type. */
    public static final PMDDescriptor DESCRIPTOR
        = new PMDDescriptor();

    /** Private constructor */
    private PMDDescriptor() {
        super("pmd");
    }

    /**
     * Create a parser for the PMD type.
     * @return a new PMD parser.
     */
    @Override
    public AbstractTypeParser createParser() {
        return new PMDParser();
    }

    /**
     * Get a list of target xml files to look for
     * for this particular type.
     * @return a list filenames to look for in the target
     *         target directory.
     */
    @Override
    public List<String> getMavenTargets() {
        List<String> ret = new ArrayList<String>();
        ret.add("pmd.xml");
        return ret;
    }
}

