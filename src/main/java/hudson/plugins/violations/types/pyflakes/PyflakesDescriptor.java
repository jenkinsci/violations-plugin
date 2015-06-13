package hudson.plugins.violations.types.pyflakes;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for Pyflakes violations type.
 */
public final class PyflakesDescriptor  extends TypeDescriptor {

    /** The descriptor for the Pyflakes violations type. */
    public static final PyflakesDescriptor DESCRIPTOR = new PyflakesDescriptor();

    private PyflakesDescriptor() {
        super("pyflakes");
    }

    /**
     * Create a parser for the Pyflakes type.
     * @return a new Pyflakes parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new PyflakesParser();
    }

}

