package hudson.plugins.violations.types.ideainspect;

import hudson.plugins.violations.TypeDescriptor;
import hudson.plugins.violations.ViolationsParser;

import java.util.ArrayList;
import java.util.List;

/**
 * The descriptor class for Intellij IDE-familiy (IDEA, PyCharm) inspector
 * violations type.
 *
 * @author <a href="mailto:sarkhipov@asdco.ru">Sergey Arkhipov</a>
 *
 */
public class IdeaInspectDescriptor extends TypeDescriptor {

    /** The descriptor for the Intellij IDEA inspector violations types. */
    public static final IdeaInspectDescriptor DESCRIPTOR = new IdeaInspectDescriptor();

    private IdeaInspectDescriptor() {
        super(IdeaInspectParser.TYPE_NAME);
    }

    /**
     * Create a parser for the Intellij IDEA inspector.
     * @return a new IDEA parser.
     */
    @Override
    public ViolationsParser createParser() {
        return new IdeaInspectParser();
    }

}
