package hudson.plugins.violations;

import hudson.plugins.violations.model.FullBuildModel;

import java.io.File;
import java.io.IOException;

/**
 * Interface that violations parsers need
 * to implement.
 */
public interface ViolationsParser {

    /**
     * Parse a violations file.
     * @param model the model to store the violations in.
     * @param projectPath the project path used for resolving paths.
     * @param fileName the name of the violations file to parse
     *                       (relative to the projectPath).
     * @param sourcePaths a list of source paths to resolve classes against
     * @throws IOException if there is an error.
     */
    void parse(
        FullBuildModel model,
        File           projectPath,
        String         fileName,
        String[]       sourcePaths)
        throws IOException;
}
