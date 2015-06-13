package hudson.plugins.violations.types.zptlint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for Zptlint violations type.
 */
public final class ZptlintDescriptor  extends TypeDescriptor {

    /** The descriptor for the Zptlint violations type. */
    public static final ZptlintDescriptor DESCRIPTOR = new ZptlintDescriptor();

    private ZptlintDescriptor() {
        super("zptlint");
    }

    /**
     * Create a parser for the Zptlint type.
     * @return a new Zptlint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new ZptlintParser();
    }

}
