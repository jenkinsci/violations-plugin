package hudson.plugins.violations.types.checkstyle;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.parse.AbstractTypeParser;

/**
 * The descriptor class for Checkstyle violations type.
 */
public final class CheckstyleDescriptor
    extends TypeDescriptor {

    /** The descriptor for the checkstyle violations type. */
    public static final CheckstyleDescriptor DESCRIPTOR
        = new CheckstyleDescriptor();

    private CheckstyleDescriptor() {
        super("checkstyle");
    }

    /**
     * Create a parser for the checkstyle type.
     * @return a new checkstyle parser.
     */
    @Override
    public AbstractTypeParser createParser() {
        return new CheckstyleParser();
    }
}

