package hudson.plugins.violations.types.codenarc;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.parse.AbstractTypeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The descriptor class for Codenarc violations type.
 * 
 * @author Robin Bramley
 */
public final class CodenarcDescriptor extends TypeDescriptor {

    /** The descriptor for the codenarc violations type. */
    public static final CodenarcDescriptor DESCRIPTOR = addDescriptor(new CodenarcDescriptor());

    private CodenarcDescriptor() {
        super("codenarc");
    }

    /**
     * Create a parser for the codenarc type.
     * 
     * @return a new codenarc parser.
     */
    @Override
    public AbstractTypeParser createParser() {
        return new CodenarcParser();
    }

    /**
     * Get a list of target xml files to look for for this particular type.
     * 
     * @return a list filenames to look for in the target target directory.
     */
    @Override
    public List<String> getMavenTargets() {
        List<String> ret = new ArrayList<String>();
        ret.add("CodeNarcXmlReport.xml");
        return ret;
    }
}
