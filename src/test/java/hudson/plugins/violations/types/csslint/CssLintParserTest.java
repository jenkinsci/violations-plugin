package hudson.plugins.violations.types.csslint;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.csslint.CssLintDescriptor.CSSLINT;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class CssLintParserTest extends JenkinsRule {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testThatCsslintFileCanBeParsed() throws Exception {
        violationsReport(CSSLINT).reportedIn("**/lint-report.xml, **/csslint-report.xml").perform()
                .assertThat("some/filename.css").wasReported().reportedViolation(1, "ALSO BOGUS", "BOGUS");
    }
}
