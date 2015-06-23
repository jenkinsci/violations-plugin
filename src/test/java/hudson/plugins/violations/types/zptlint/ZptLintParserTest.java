package hudson.plugins.violations.types.zptlint;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.zptlint.ZptlintDescriptor.ZPTLINT;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class ZptLintParserTest extends JenkinsRule {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testThatZptLintFileCanBeParsed() throws Exception {
        violationsReport(ZPTLINT).reportedIn("**/zptlint-report.log").perform().assertThat("cpplint.py").wasReported()
                .reportedViolation(4796, "C", "abc def ghe '\" 123")
                .reportedViolation(4797, "C", "abc '\" 123 def ghe");
    }
}
