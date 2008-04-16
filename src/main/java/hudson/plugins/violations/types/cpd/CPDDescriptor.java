package hudson.plugins.violations.types.cpd;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

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
    public ViolationsParser createParser() {
        return new CPDParser();
    }

}

