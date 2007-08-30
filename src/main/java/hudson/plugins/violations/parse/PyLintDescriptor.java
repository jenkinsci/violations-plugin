package hudson.plugins.violations.parse;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for PyLint violations type.
 */
public final class PyLintDescriptor  extends TypeDescriptor {

	/** The descriptor for the PyLint violations type. */
    public static final PyLintDescriptor DESCRIPTOR = new PyLintDescriptor();

    private PyLintDescriptor() {
        super("pylint");
    }

    /**
     * Create a parser for the PyLint type.
     * @return a new PyLint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new PyLintParser();
    }

}

