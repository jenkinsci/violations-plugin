package hudson.plugins.violations.types.simian;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

/**
 * Descriptor for the Simian violation type.
 */
public class SimianDescriptor extends TypeDescriptor {
    
    /** The one and only instance of the descriptor. */
    public static final SimianDescriptor DESCRIPTOR = new SimianDescriptor();
    
    private SimianDescriptor() {
        super(SimianParser.TYPE_NAME);
    }

    /**
     * Create a parser for the Simian violation type.
     * @return a new Simian parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new SimianParser();
    }
}
