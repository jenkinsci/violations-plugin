package hudson.plugins.violations.types.cpplint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for CppLint violations type.
 */
public final class CppLintDescriptor  extends TypeDescriptor {

    /** The descriptor for the CppLint violations type. */
    public static final CppLintDescriptor DESCRIPTOR = new CppLintDescriptor();

    private CppLintDescriptor() {
        super("cpplint");
    }

    /**
     * Create a parser for the CppLint type.
     * @return a new CppLint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new CppLintParser();
    }

}

