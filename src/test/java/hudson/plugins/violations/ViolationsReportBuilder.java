package hudson.plugins.violations;

import static hudson.plugins.violations.MagicNames.VIOLATIONS;
import static hudson.plugins.violations.ViolationsPublisher.createBuildAction;
import static hudson.plugins.violations.ViolationsReportAsserter.assertThat;
import static java.lang.Thread.currentThread;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hudson.FilePath;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;

import java.io.File;

public class ViolationsReportBuilder {

    private String sourcePathPattern;
    private final TypeDescriptor typeDescriptor;

    private ViolationsReportBuilder(TypeDescriptor typeDescriptor) {
        this.typeDescriptor = typeDescriptor;
    }

    public static ViolationsReportBuilder violationsReport(String typeDescriptor) {
        return new ViolationsReportBuilder(TypeDescriptor.TYPES.get(typeDescriptor));
    }

    public ViolationsReportBuilder reportedIn(String sourcePathPattern) {
        this.sourcePathPattern = sourcePathPattern;
        return this;
    }

    public ViolationsReportAsserter perform() throws Exception {
        ViolationsConfig config = new ViolationsConfig();
        config.setSourcePathPattern(sourcePathPattern);
        TypeConfig typeConfig = new TypeConfig(typeDescriptor.getName());
        typeConfig.setPattern(sourcePathPattern);
        config.getTypeConfigs().put(typeDescriptor.getName(), typeConfig);

        FilePath workspace = new FilePath(projectRootDir());
        FilePath targetPath = new FilePath(new File(projectRootDir().getPath() + "/" + VIOLATIONS));
        FilePath htmlPath = new FilePath(projectRootDir());
        AbstractBuild<?, ?> build = mock(Build.class);
        when(build.getRootDir()).thenReturn(projectRootDir());
        BuildListener listener = mock(BuildListener.class);
        ViolationsReport violationsReport = createBuildAction(workspace, targetPath, htmlPath, config, build, listener)
                .getReport();
        return assertThat(violationsReport, typeDescriptor);
    }

    private File projectRootDir() {
        return new File(currentThread().getContextClassLoader().getResource("rootDir.txt").getPath()).getParentFile();
    }
}
