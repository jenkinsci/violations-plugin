package hudson.plugins.violations.types.checkstyle;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.checkstyle.CheckstyleDescriptor.CHECKSTYLE;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class CheckstyleParserTest extends JenkinsRule {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testThatCheckstyleCanBeParsed() throws Exception {
        violationsReport(CHECKSTYLE)
                .reportedIn("**/checkstyle-report.xml")
                .perform()
                .assertThat("path/to/jenkins/workspace/jobname/module/src/main/java/se/bjurr/code/CheckstyleFile.java")
                .wasReported()
                .reportedViolation(60, "TrailingCommentCheck", "Checkstyle \"comment\".")
                .reportedViolation(64, "MemberNameCheck", "Other 'checkstyle' comment.")
                .and()
                .assertThat(
                        "path/to/jenkins/workspace/jobname/module/src/main/java/se/bjurr/UnchangedFileInCheckstyle.java")
                .wasReported()
                .reportedViolation(60, "TrailingCommentCheck",
                        "UnchangedFileInCheckstyle comment in checkstyle this one is not changed in PR.");
    }
}
