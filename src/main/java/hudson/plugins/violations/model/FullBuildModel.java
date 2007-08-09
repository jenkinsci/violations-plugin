package hudson.plugins.violations.model;

import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;


/**
 * Class containing all the violations for a build.
 * built up during parsing of the violation xml files.
 */
public class FullBuildModel {

    private SortedMap<String, SortedSet<FileSummary>> typeMap
        = new TreeMap<String, SortedSet<FileSummary>>();
    private Map<String, FullFileModel> fileModels
        = new HashMap<String, FullFileModel>();


    /**
     * get the map of type to file summaries.
     * @return the map of type to file summaries.
     */
    public SortedMap<String, SortedSet<FileSummary>> getTypeMap() {
        return typeMap;
    }
    /**
     * get the map of filename to file model.
     * @return the map of filename to full file models.
     */
    public Map<String, FullFileModel> getFileModelMap() {
        return fileModels;
    }


    /**
     * For a particular type get the total number of violations.
     * @param type the violations type.
     * @return the total number.
     */
    public int getCountNumber(String type) {
        SortedSet<FileSummary> files = typeMap.get(type);
        if (files == null) {
            return 0;
        }
        int count = 0;
        for (FileSummary file: files) {
            count += file.getViolations().size();
        }
        return count;
    }

    /**
     * Add in a type.
     * Called at build time to ensure enties.
     * @param type the violation type to add.
     */
    public void addType(String type) {
        getTypeSummary(type);
    }

    /**
     * Get the set of file summaries for a particular type.
     * @param type the type to get the summaries for.
     * @return the file summary set.
     */
    public SortedSet<FileSummary> getTypeSummary(String type) {
        SortedSet<FileSummary> ret = typeMap.get(type);
        if (ret == null) {
            ret = new TreeSet<FileSummary>();
            typeMap.put(type, ret);
        }
        return ret;
    }

    /**
     * Get a full file model for a particular file name.
     * @param displayName the name to use to refer to the file.
     * @return the model for the file.
     */
    public FullFileModel getFileModel(String displayName) {
        FullFileModel ret = fileModels.get(displayName);
        if (ret == null) {
            ret = new FullFileModel();
            ret.setDisplayName(displayName);
            fileModels.put(displayName, ret);
        }
        return ret;
    }

    /**
     * Called at the end of a build.
     * This sets up file summary records.
     */
    public void cleanup() {
        for (FullFileModel file: fileModels.values()) {
            for (String type: file.getTypeMap().keySet()) {
                SortedSet<FileSummary> set = getTypeSummary(type);
                set.add(new FileSummary(file, file.getTypeMap().get(type)));
            }
        }
    }
}
