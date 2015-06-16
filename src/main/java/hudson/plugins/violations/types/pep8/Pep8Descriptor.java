package hudson.plugins.violations.types.pep8;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for the PEP 8 violations type.
 */
public final class Pep8Descriptor extends TypeDescriptor {

    public static final String PEP8 = "pep8";

    public Pep8Descriptor() {
        super(PEP8);
    }

    /**
     * Create a parser for the PEP 8 type.
     *
     * @return a new PEP 8 parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new Pep8Parser();
    }

}
