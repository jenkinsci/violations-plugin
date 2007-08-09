package hudson.plugins.violations;

import java.util.TreeMap;

import hudson.plugins.violations.parse.AbstractTypeParser;
import hudson.plugins.violations.parse.CheckstyleParser;
import hudson.plugins.violations.parse.PMDParser;
import hudson.plugins.violations.parse.FindBugsParser;
import hudson.plugins.violations.parse.CPDParser;

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
    public abstract AbstractTypeParser getParser();

    /**  The map of types to type descriptors. */
    public static final TreeMap<String, TypeDescriptor> TYPES =
        new TreeMap<String, TypeDescriptor>();

    static {
        TYPES.put(
            "checkstyle", new TypeDescriptor("checkstyle") {
                public AbstractTypeParser getParser() {
                    return new CheckstyleParser();
                }
            });
        TYPES.put(
            "pmd", new TypeDescriptor("pmd") {
                public AbstractTypeParser getParser() {
                    return new PMDParser();
                }
            });
        TYPES.put(
            "findbugs", new TypeDescriptor("findbugs") {
                public AbstractTypeParser getParser() {
                    return new FindBugsParser();
                }
            });
        TYPES.put(
            "cpd", new TypeDescriptor("cpd") {
                public AbstractTypeParser getParser() {
                    return new CPDParser();
                }
            });
    }
}


