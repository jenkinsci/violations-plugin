package hudson.plugins.violations.types.stylecop;

import java.util.ArrayList;
import java.util.List;

import hudson.plugins.violations.TypeDescriptor;

/**
 * The descriptor class for StyleCop (MS Source Analysis) violations type.
 * http://code.msdn.microsoft.com/sourceanalysis/
 */
public final class StyleCopDescriptor extends TypeDescriptor {

    /** The descriptor for the MS Source Analysis violations type. */
    public static final StyleCopDescriptor DESCRIPTOR = new StyleCopDescriptor();

    private StyleCopDescriptor() {
        super(StyleCopParser.TYPE_NAME);
    }

    @Override
    public StyleCopParser createParser() {
        return new StyleCopParser();
    }

    @Override
    public List<String> getMavenTargets() {
        List<String> ret = new ArrayList<String>();
        ret.add("SourceAnalysisViolations.xml"); // "good" report
        return ret;
    }
}
