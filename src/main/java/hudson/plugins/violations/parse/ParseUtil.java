package hudson.plugins.violations.parse;

import java.io.*;
import java.util.*;


public class ParseUtil {

    /**
     * Resolve an absolute name agaist the project path.
     * @param absoluteName the absolute name.
     * @return a path relative to the project path or an absolute
     *         name if the name cannot be resolved.
     */
    public static String resolveAbsoluteName(
        File projectPath, String absoluteName) {
        String remote = projectPath.getAbsolutePath().replace('\\', '/');
        String lcRemote = remote.toLowerCase(Locale.US);
        String name = absoluteName.replace('\\', '/');
        String lcName = name.toLowerCase(Locale.US);
        if (lcName.startsWith(lcRemote)) {
            name = name.substring(lcRemote.length());
        } else {
            // for absolute name discard dos drive
            int colPos = name.indexOf(':');
            int dirPos = name.indexOf('/');
            if (colPos != -1 && (dirPos == -1 || dirPos > colPos)) {
                name = name.substring(colPos + 1);
            }
        }
        // if name starts with a / strip it.
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;
    }
}

