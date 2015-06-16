package hudson.plugins.violations.types.xmllint;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * The descriptor class for Xmllint violations type.
 */
public final class XmllintDescriptor extends TypeDescriptor {

    public static final String XMLLINT = "xmllint";

    public XmllintDescriptor() {
        super(XMLLINT);
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
