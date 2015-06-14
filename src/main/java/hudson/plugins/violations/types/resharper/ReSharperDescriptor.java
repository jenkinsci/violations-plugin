package hudson.plugins.violations.types.resharper;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

import java.util.ArrayList;
import java.util.List;

public class ReSharperDescriptor extends TypeDescriptor {

    public static final ReSharperDescriptor DESCRIPTOR = addDescriptor(new ReSharperDescriptor());

    private ReSharperDescriptor() {
        super("resharper");
    }

    @Override
    public ViolationsParser createParser() {
        return new ReSharperParser();
    }

    /**
     * Get a list of target xml files to look for for this particular type.
     * 
     * @return a list filenames to look for in the target target directory.
     */
    @Override
    public List<String> getMavenTargets() {
        final List<String> ret = new ArrayList<String>();
        ret.add("resharper.xml");
        return ret;
    }
}
