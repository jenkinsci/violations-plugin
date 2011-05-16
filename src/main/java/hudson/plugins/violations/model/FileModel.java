package hudson.plugins.violations.model;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * A class contains the violations for a particular
 * file. The number of violations are limited.
 */
public class FileModel extends AbstractFileModel {
    private SortedMap<String, LimitType> limitTypeMap
        = new TreeMap<String, LimitType>();

    /**
     * Get a map of type to limit type.
     * @return the map.
     */
    public SortedMap<String, LimitType> getLimitTypeMap() {
        return limitTypeMap;
    }

    /**
     * Add summary object for a type of violation.
     * @param type the type of violation.
     * @param number the number of violations of that type in this file.
     * @param suppressed the number of violations not shown for this file.
     */
    public void addLimitType(String type, int number, int suppressed) {
        LimitType l = new LimitType();
        l.type = type;
        l.number = number;
        l.suppressed = suppressed;
        limitTypeMap.put(type, l);
    }

    /**
     * Add a violation to the file model.
     * @param v the violation to add.
     */
    @Override
    public void addViolation(Violation v) {
        TreeSet<Violation> set = getTypeMap().get(v.getType());
        if (set == null) {
            set = new TreeSet<Violation>();
            getTypeMap().put(v.getType(), set);
        }
        set.add(v);

        Set<Violation> lineSet = getLineViolationMap().get(v.getLine());
        if (lineSet == null) {
            lineSet = new TreeSet<Violation>();
            getLineViolationMap().put(v.getLine(), lineSet);
        }
        lineSet.add(v);
    }

    /**
     * A overview class for each type.
     */
    public static class LimitType {
        private String type;
        private int    number;
        private int    suppressed;
        /**
         * Get the type,
         * @return the type.
         */
        public String getType() {
            return type;
        }

        /**
         * Get the number of violations.
         * @return the number.
         */
        public int getNumber() {
            return number;
        }

        /**
         * Get the number of suppressed violations.
         * @return the number.
         */
        public int getSuppressed() {
            return suppressed;
        }
    }
}
