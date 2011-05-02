package hudson.plugins.violations.generate;

import hudson.FilePath;
import hudson.plugins.violations.ViolationsConfig;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;

import java.io.IOException;

/**
 * Generate the xml files for a publish invocation.
 */
public class GenerateXML {
    private final FilePath targetDir;
    private final FullBuildModel model;
    private final ViolationsConfig config;

    /**
     * Create and configure the GenerateXML object.
     * @param targetDir the directory to place the files in.
     * @param model     the full build model to write out.
     * @param config    the current configation, used for limiting the
     *                  violations per file written out.
     */
    public GenerateXML(FilePath targetDir, FullBuildModel model,
                       ViolationsConfig config) {
        this.targetDir = targetDir;
        this.model = model;
        this.config = config;
    }

    /**
     * Write out the files.
     * @throws IOException if there is a problem writing the files.
     * @throws InterruptedException if there is a problem with the targetDir.
     */
    public void execute() throws IOException, InterruptedException {
        new ExecuteFilePath(
            targetDir, "violations.xml",
            new OutputBuildModel(model)).execute();
        for (FullFileModel file: model.getFileModelMap().values()) {
            if (file.getTypeMap().size() == 0) {
                continue;
            }
            new ExecuteFilePath(
                targetDir, "file/" + file.getDisplayName() + ".xml",
                new OutputFileModel(file, config)).execute();
        }
    }
}
