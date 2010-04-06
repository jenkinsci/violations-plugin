package hudson.plugins.violations.types.jcreport;

import java.util.ArrayList;
import java.util.List;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.parse.AbstractTypeParser;

/**
 * The descriptor class for jcreport violations type.
 * http://www.jcoderz.org/fawkez/wiki/JcReport 
 * @author Andreas.Mandel@gmail.com
 */
public final class JcReportDescriptor
    extends TypeDescriptor {

    /** The descriptor for the jcreport violations type. */
    public static final JcReportDescriptor DESCRIPTOR
        = new JcReportDescriptor();

    private JcReportDescriptor() {
        super("jcreport");
    }

    /**
     * Create a parser for the jcreport type.
     * @return a new jcreport parser.
     */
    @Override
    public AbstractTypeParser createParser() {
        return new JcReportParser();
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
        ret.add("jcoderz-report.xml");
        return ret;
    }
}

