package hudson.plugins.violations.model;

import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.Collection;

import java.io.File;
import hudson.plugins.violations.render.FileModelProxy;


/**
 * A model of the violations of the build used during rendering time.
 */
public class BuildModel {

    private File  xmlRoot;
    private Map<String, FileModelProxy> fileModelMap
        = new HashMap<String, FileModelProxy>();

    private SortedMap<String, SortedSet<FileCount>> typeMap
        = new TreeMap<String, SortedSet<FileCount>>();

    private SortedMap<String, TypeCount> typeCountMap;


    /**
     * Create a model of the violations.
     * @param xmlFile the file used to create this (used to
     *                get the directory for parsing the per file
     *                xml files on demand).
     */
    public BuildModel(File xmlFile) {
        this.xmlRoot = xmlFile.getParentFile();
    }

    /**
     * Get the map of type to file counts.
     * @return the map of type to file counts.
     */
    public  SortedMap<String, SortedSet<FileCount>> getTypeMap() {
        return typeMap;
    }

    /**
     * Get the file model map.
     * @return a map of name to file model proxy.
     */
    public Map<String, FileModelProxy> getFileModelMap() {
        return fileModelMap;
    }

    /**
     * Get the collection of type counts.
     * @return the collection of type counts.
     */
    public  Collection<TypeCount> getTypeCounts() {
        if (typeCountMap == null) {
            typeCountMap = new TreeMap<String, TypeCount>();
            for (String t: typeMap.keySet()) {
                int count = 0;
                for (FileCount fc: typeMap.get(t)) {
                    count += fc.getCount();
                }
                typeCountMap.put(
                    t, new TypeCount(t, typeMap.get(t).size(), count));
            }
        }
        return typeCountMap.values();
    }

    /**
     * Get the type count map.
     * @return the type count map.
     */
    public Map<String, TypeCount> getTypeCountMap() {
        getTypeCounts();
        return typeCountMap;
    }

    /**
     * A class used in displaying number of violations and files
     * in violation.
     */
    public static class TypeCount {
        private final String name;
        private final int    count;
        private final int    numberFiles;

        /**
         * Constructor for TypeCount.
         * @param name the type name.
         * @param numberFiles the number of files in violation.
         * @param count the number of violations.
         */
        public TypeCount(String name, int numberFiles, int count) {
            this.name = name;
            this.numberFiles = numberFiles;
            this.count = count;
        }

        /**
         * Get the type name.
         * @return the type name.
         */
        public String getName() {
            return name;
        }

        /**
         * Get the number of violations of this type.
         * @return the number.
         */
        public int getCount() {
            return count;
        }

        /**
         * Get the number of file that contain violations of this type.
         * @return the number.
         */
        public int getNumberFiles() {
            return numberFiles;
        }
    }

    /**
     * get a set of file counts for a type.
     * @param type the type to ge for.
     * @return a set of file counts.
     */
    public SortedSet<FileCount> getFileCounts(String type) {
        SortedSet<FileCount> ret = typeMap.get(type);
        if (ret == null) {
            ret = new TreeSet<FileCount>();
            typeMap.put(type, ret);
        }
        return ret;
    }

    private FileModelProxy  getFileNameProxy(String name) {
        FileModelProxy proxy = fileModelMap.get(name);
        if (proxy != null) {
            return proxy;
        }
        File xmlFile = new File(
            xmlRoot, "file/" + name + ".xml");
        fileModelMap.put(name, new FileModelProxy(xmlFile));
        proxy = fileModelMap.get(name);
        return proxy;
    }

    /**
     * Add a file count.
     * @param type the type.
     * @param name the filename.
     * @param count the number of violations.
     */
    public void addFileCount(String type, String name, int[] count) {
        // Windows needs this replacement
        name = name.replace("\\", "/");
        FileModelProxy proxy = getFileNameProxy(name);
        getFileCounts(type).add(new FileCount(name, count, proxy));
    }

    /**
     * A class of file name to count mapping.
     */
    public static class FileCount implements Comparable<FileCount> {
        private final String name;
        private final int    totalCount;
        private final int[]  counts;
        private final FileModelProxy proxy;

        /**
         * Create a FileCount object.
         * @param name the name of the file.
         * @param counts the numbers of violations (of a particular type) in the
         *              file.
         * @param proxy the associated file proxy (used during rendering).
         */
        public FileCount(String name, int[] counts, FileModelProxy proxy) {
            this.name = name;
            this.counts = counts;
            int t = 0;
            for (int i = 0; i < counts.length; ++i) {
                t += counts[i];
            }
            this.totalCount = t;
            this.proxy = proxy;
        }

        /**
         * Get the name of the file.
         * @return the filename.
         */
        public String getName() {
            return name;
        }

        /**
         * Get the number of violations.
         * @return the number.
         */
        public int getCount() {
            return totalCount;
        }

        /**
         * Get the number of high severity violations.
         * @return the number.
         */
        public int getHigh() {
            return counts[Severity.HIGH_VALUE];
        }

        /**
         * Get the number of medium severity violations.
         * @return the number.
         */
        public int getMedium() {
            return counts[Severity.MEDIUM_HIGH_VALUE]
                + counts[Severity.MEDIUM_VALUE]
                + counts[Severity.MEDIUM_LOW_VALUE];
        }

        /**
         * Get the number of low severity violations.
         * @return the number.
         */
        public int getLow() {
            return counts[Severity.LOW_VALUE];
        }

        /**
         * Get the associated file model proxy.
         * @return the file model proxy.
         */
        public FileModel getFileModel() {
            return proxy.getFileModel();
        }

        /**
         * Compare to another FileCount object.
         * @param other the other file count object.
         * @return 0 if they are the same, -1 if count is greater
         *        1 if count is less, if counts are the same
         *        use the name for comparison.
         */
        public int compareTo(FileCount other) {
            if (this == other) {
                return 0;
            }
            if (totalCount > other.totalCount) {
                return -1;
            } else if (totalCount < other.totalCount) {
                return 1;
            }
            return name.compareTo(other.name);
        }
    }
}
