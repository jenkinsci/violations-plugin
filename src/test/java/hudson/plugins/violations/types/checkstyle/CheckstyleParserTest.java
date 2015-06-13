package hudson.plugins.violations.types.checkstyle;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.checkstyle.CheckstyleDescriptor.DESCRIPTOR;
import jenkins.model.Jenkins;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Jenkins.class })
public class CheckstyleParserTest {
    @Test
    public void testThatCheckstyleCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/checkstyle-report.xml")
                .perform()
                .assertThat(
                        "path/to/jenkins/workspace/jobname/module/src/main/java/se/bjurr/code/CheckstyleFile.java")
                .wasReported()
                .reportedViolation(60, "TrailingCommentCheck",
                        "Checkstyle \"comment\".")
                .reportedViolation(64, "MemberNameCheck",
                        "Other 'checkstyle' comment.")
                .and()
                .assertThat(
                        "path/to/jenkins/workspace/jobname/module/src/main/java/se/bjurr/UnchangedFileInCheckstyle.java")
                .wasReported()
                .reportedViolation(
                        60,
                        "TrailingCommentCheck",
                        "UnchangedFileInCheckstyle comment in checkstyle this one is not changed in PR.");
    }
}
