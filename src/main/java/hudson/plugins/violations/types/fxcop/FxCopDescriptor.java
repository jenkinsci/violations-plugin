package hudson.plugins.violations.types.fxcop;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for FxCop violations type.
 *
 * @author Erik Ramfelt
 */
public final class FxCopDescriptor extends TypeDescriptor {
    /** The descriptor for the FxCop violations type. */
    public static final FxCopDescriptor DESCRIPTOR = addDescriptor(new FxCopDescriptor());

    private FxCopDescriptor() {
        super("fxcop");
    }

    /**
     * Create a parser for the FxCop type.
     * 
     * @return a new FxCop parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new FxCopParser();
    }
}
