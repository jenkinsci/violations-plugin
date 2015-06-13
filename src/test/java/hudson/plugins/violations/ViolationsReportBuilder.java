package hudson.plugins.violations;

import static hudson.plugins.violations.MagicNames.VIOLATIONS;
import static hudson.plugins.violations.ViolationsPublisher.createBuildAction;
import static hudson.plugins.violations.ViolationsReportAsserter.assertThat;
import static java.lang.Thread.currentThread;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;
import hudson.FilePath;
import hudson.model.Build;
import hudson.model.AbstractBuild;

import java.io.File;

import jenkins.model.Jenkins;

import org.powermock.api.mockito.PowerMockito;

public class ViolationsReportBuilder {
    private String sourcePathPattern;
    private TypeDescriptor typeDescriptor;

    private ViolationsReportBuilder(TypeDescriptor typeDescriptor) {
        this.typeDescriptor = typeDescriptor;
    }

    public static ViolationsReportBuilder violationsReport(
            TypeDescriptor typeDescriptor) {
        return new ViolationsReportBuilder(typeDescriptor);
    }

    public ViolationsReportBuilder reportedIn(String sourcePathPattern) {
        this.sourcePathPattern = sourcePathPattern;
        return this;
    }

    public ViolationsReportAsserter perform() throws Exception {
        mockJenkins();

        ViolationsConfig config = new ViolationsConfig();
        config.setSourcePathPattern(sourcePathPattern);
        TypeConfig typeConfig = new TypeConfig(typeDescriptor.getName());
        typeConfig.setPattern(sourcePathPattern);
        config.getTypeConfigs().put(typeDescriptor.getName(), typeConfig);

        FilePath workspace = new FilePath(projectRootDir());
        FilePath targetPath = new FilePath(new File(projectRootDir().getPath()
                + "/" + VIOLATIONS));
        FilePath htmlPath = new FilePath(projectRootDir());
        AbstractBuild<?, ?> build = mock(Build.class);
        when(build.getRootDir()).thenReturn(projectRootDir());
        ViolationsReport violationsReport = createBuildAction(workspace,
                targetPath, htmlPath, config, build).getReport();
        return assertThat(violationsReport, typeDescriptor);
    }

    private Jenkins mockJenkins() {
        Jenkins mockedJenkins = mock(Jenkins.class);
        spy(Jenkins.class);
        PowerMockito.when(Jenkins.getInstance()).thenReturn(mockedJenkins);
        return mockedJenkins;
    }

    private File projectRootDir() {
        return new File(currentThread().getContextClassLoader()
                .getResource("rootDir.txt").getPath()).getParentFile();
    }
}
