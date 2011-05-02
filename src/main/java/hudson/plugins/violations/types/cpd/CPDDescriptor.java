package hudson.plugins.violations.types.cpd;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The descriptor class for CPD violations type.
 */
public final class CPDDescriptor
    extends TypeDescriptor {
    /** The descriptor for the CPD violations type. */
    public static final CPDDescriptor DESCRIPTOR
        = new CPDDescriptor();

    private CPDDescriptor() {
        super("cpd");
    }

    /**
     * Create a parser for the CPD type.
     * @return a new CPD parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new CPDParser();
    }

    /**
     * Get a list of target xml files to look for
     * for this particular type.
     * @return a list filenames to look for in the target
     *         target directory.
     */
    @Override
    public List<String> getMavenTargets() {
        List<String> ret = new ArrayList<String>();
        ret.add("cpd.xml");
        return ret;
    }

}

