package hudson.plugins.violations.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that helps finding a file from a list of source paths.
 * Type parsers can use this to find the absolute file path for a relative
 * file name.
 */
public class AbsoluteFileFinder {
	private transient List<String> sourcePaths = new ArrayList<String>();

	public void addSourcePath(String path) {
		sourcePaths.add(path);
	}
	
	public void addSourcePaths(String[] paths) {
		for (String path : paths) {
			sourcePaths.add(path);
		}
	}

    public File getFileForName(String name) {
        for (String p : sourcePaths) {
            File f = new File(new File(p), name);
            if (f.isFile()) {
                return f;
            }
        }
        return null;
    }
}
