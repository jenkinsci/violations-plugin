package hudson.plugins.violations;

import hudson.plugins.violations.types.checkstyle.CheckstyleDescriptor;
import hudson.plugins.violations.types.codenarc.CodenarcDescriptor;
import hudson.plugins.violations.types.cpd.CPDDescriptor;
import hudson.plugins.violations.types.cpplint.CppLintDescriptor;
import hudson.plugins.violations.types.csslint.CssLintDescriptor;
import hudson.plugins.violations.types.findbugs.FindBugsDescriptor;
import hudson.plugins.violations.types.fxcop.FxCopDescriptor;
import hudson.plugins.violations.types.gendarme.GendarmeDescriptor;
import hudson.plugins.violations.types.jcreport.JcReportDescriptor;
import hudson.plugins.violations.types.jslint.JsLintDescriptor;
import hudson.plugins.violations.types.pep8.Pep8Descriptor;
import hudson.plugins.violations.types.perlcritic.PerlCriticDescriptor;
import hudson.plugins.violations.types.pmd.PMDDescriptor;
import hudson.plugins.violations.types.pyflakes.PyflakesDescriptor;
import hudson.plugins.violations.types.pylint.PyLintDescriptor;
import hudson.plugins.violations.types.resharper.ReSharperDescriptor;
import hudson.plugins.violations.types.simian.SimianDescriptor;
import hudson.plugins.violations.types.stylecop.StyleCopDescriptor;
import hudson.plugins.violations.types.xmllint.XmllintDescriptor;
import hudson.plugins.violations.types.zptlint.ZptlintDescriptor;

import java.util.List;
import java.util.TreeMap;

/**
 * A descriptor for a violation type. This contains a name and an parser class
 * for the violation type. The types are (currently) also contains in the class
 * as a static - TYPES.
 */
public abstract class TypeDescriptor {
    private final String name;

    /**
     * Create a type descriptor for a type.
     *
     * @param name
     *            the name of the type.
     */
    protected TypeDescriptor(final String name) {
        this.name = name;
    }

    /**
     * Get the name of the type.
     *
     * @return the type name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get a new parser for the type.
     *
     * @return a new parser object.
     */
    public abstract ViolationsParser createParser();

    /** The map of types to type descriptors. */
    public static TreeMap<String, TypeDescriptor> TYPES = new TreeMap<String, TypeDescriptor>();

    /**
     * Add a violations type descriptor.
     *
     * @param t
     *            the violations type descriptor to add.
     */
    private static void addDescriptor(final TypeDescriptor t) {
        TYPES.put(t.getName(), t);
    }

    /**
     * Get a detailed description of a violation source.
     *
     * @param source
     *            the code name for the violation.
     * @return a detailed description, if available, null otherwise.
     */
    public String getDetailForSource(final String source) {
        return null;
    }

    /**
     * Get a list of target xml files to look for for this particular type.
     *
     * @return a list filenames to look for in the target target directory.
     */
    public List<String> getMavenTargets() {
        return null;
    }

    static {
        addDescriptor(new FindBugsDescriptor());
        addDescriptor(new PMDDescriptor());
        addDescriptor(new CPDDescriptor());
        addDescriptor(new CheckstyleDescriptor());
        addDescriptor(new PyLintDescriptor());
        addDescriptor(new CppLintDescriptor());
        addDescriptor(new FxCopDescriptor());
        addDescriptor(new SimianDescriptor());
        addDescriptor(new StyleCopDescriptor());
        addDescriptor(new GendarmeDescriptor());
        addDescriptor(new JcReportDescriptor());
        addDescriptor(new JsLintDescriptor());
        addDescriptor(new CssLintDescriptor());
        addDescriptor(new CodenarcDescriptor());
        addDescriptor(new Pep8Descriptor());
        addDescriptor(new PerlCriticDescriptor());
        addDescriptor(new ReSharperDescriptor());
        addDescriptor(new PyflakesDescriptor());
        addDescriptor(new XmllintDescriptor());
        addDescriptor(new ZptlintDescriptor());
    }
}
