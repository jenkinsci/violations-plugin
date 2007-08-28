package hudson.plugins.violations;

import java.util.TreeMap;

import hudson.plugins.violations.parse.AbstractTypeParser;
import hudson.plugins.violations.parse.CheckstyleDescriptor;
import hudson.plugins.violations.parse.PMDDescriptor;
import hudson.plugins.violations.parse.FindBugsDescriptor;
import hudson.plugins.violations.parse.CPDDescriptor;

/**
 * A descriptor for a violation type.
 * This contains a name and an parser class for the violation type.
 * The types are (currently) also contains in the class as
 * a static - TYPES.
 */
public abstract class TypeDescriptor {
    private final String name;

    /**
     * Create a type descriptor for a type.
     * @param name the name of the type.
     */
    protected TypeDescriptor(String name) {
        this.name = name;
    }

    /**
     * Get the name of the type.
     * @return the type name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get a new parser for the type.
     * @return a new parser object.
     */
    public abstract ViolationsParser createParser();

    /**  The map of types to type descriptors. */
    public static final TreeMap<String, TypeDescriptor> TYPES =
        new TreeMap<String, TypeDescriptor>();

    /**
     * Add a violations type descriptor.
     * @param t the violations type descriptor to add.
     */
    public static void addDescriptor(TypeDescriptor t) {
        TYPES.put(t.getName(), t);
    }

    /**
     * Get a detailed description of a violation source.
     * @param source the code name for the violation.
     * @return a detailed description, if available, null otherwise.
     */
    public String getDetailForSource(String source) {
        return null;
    }

    static {
        addDescriptor(FindBugsDescriptor.DESCRIPTOR);
        addDescriptor(PMDDescriptor.DESCRIPTOR);
        addDescriptor(CPDDescriptor.DESCRIPTOR);
        addDescriptor(CheckstyleDescriptor.DESCRIPTOR);
    }
}


