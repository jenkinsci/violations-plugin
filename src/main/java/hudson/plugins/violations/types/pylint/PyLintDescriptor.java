package hudson.plugins.violations.types.pylint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for PyLint violations type.
 */
public final class PyLintDescriptor extends TypeDescriptor {

    public static final String PYLINT = "pylint";

    public PyLintDescriptor() {
        super(PYLINT);
    }

    /**
     * Create a parser for the PyLint type.
     *
     * @return a new PyLint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new PyLintParser();
    }

}
