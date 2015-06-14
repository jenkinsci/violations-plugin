package hudson.plugins.violations.types.xmllint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for Xmllint violations type.
 */
public final class XmllintDescriptor extends TypeDescriptor {

    /** The descriptor for the Xmllint violations type. */
    public static final XmllintDescriptor DESCRIPTOR = addDescriptor(new XmllintDescriptor());

    private XmllintDescriptor() {
        super("xmllint");
    }

    /**
     * Create a parser for the Xmllint type.
     * 
     * @return a new Xmllint parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new XmllintParser();
    }

}
