package hudson.plugins.violations.types.pyflakes;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for Pyflakes violations type.
 */
public final class PyflakesDescriptor extends TypeDescriptor {

    public static final String PYFLAKES = "pyflakes";

    public PyflakesDescriptor() {
        super(PYFLAKES);
    }

    /**
     * Create a parser for the Pyflakes type.
     *
     * @return a new Pyflakes parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new PyflakesParser();
    }

}
