package hudson.plugins.violations.types.zptlint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for Zptlint violations type.
 */
public final class ZptlintDescriptor extends TypeDescriptor {

    public static final String ZPTLINT = "zptlint";

    public ZptlintDescriptor() {
        super(ZPTLINT);
    }

    /**
     * Create a parser for the Zptlint type.
     *
     * @return a new Zptlint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new ZptlintParser();
    }

}
