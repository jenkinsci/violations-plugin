package hudson.plugins.violations.types.pmd;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.parse.AbstractTypeParser;

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

}

