package hudson.plugins.violations.types.cpplint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for CppLint violations type.
 */
public final class CppLintDescriptor extends TypeDescriptor {

    public static final String CPPLINT = "cpplint";

    public CppLintDescriptor() {
        super(CPPLINT);
    }

    /**
     * Create a parser for the CppLint type.
     *
     * @return a new CppLint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new CppLintParser();
    }

}
