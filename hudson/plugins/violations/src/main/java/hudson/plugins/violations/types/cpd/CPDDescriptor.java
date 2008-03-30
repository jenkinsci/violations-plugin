package hudson.plugins.violations.types.cpd;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.parse.AbstractTypeParser;

/**
 * The descriptor class for CPD violations type.
 */
public final class CPDDescriptor
    extends TypeDescriptor {
    /** The descriptor for the CPD violations type. */
    public static final CPDDescriptor DESCRIPTOR
        = new CPDDescriptor();

    private CPDDescriptor() {
        super("cpd");
    }

    /**
     * Create a parser for the CPD type.
     * @return a new CPD parser.
     */
    @Override
    public AbstractTypeParser createParser() {
        return new CPDParser();
    }

}

