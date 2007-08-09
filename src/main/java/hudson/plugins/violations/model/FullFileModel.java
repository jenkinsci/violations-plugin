package hudson.plugins.violations.model;

import java.util.TreeSet;

/**
 * A model of the violations in the a file.
 * This is used during project action and it
 * contains all the violations found.
 * A subset of these are written to an xml file later.
 */
public class FullFileModel extends AbstractFileModel {

    /**
     * Add a violation to the file model.
     * @param violation the violation to add.
     */
    @Override
    public void addViolation(Violation violation) {
        TreeSet<Violation> set = getTypeMap().get(violation.getType());
        if (set == null) {
            set = new TreeSet<Violation>();
            getTypeMap().put(violation.getType(), set);
        }
        set.add(violation);
    }
}
