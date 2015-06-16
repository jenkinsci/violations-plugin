package hudson.plugins.violations.types.fxcop;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for FxCop violations type.
 *
 * @author Erik Ramfelt
 */
public final class FxCopDescriptor extends TypeDescriptor {
    public static final String FXCOP = "fxcop";

    public FxCopDescriptor() {
        super(FXCOP);
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
