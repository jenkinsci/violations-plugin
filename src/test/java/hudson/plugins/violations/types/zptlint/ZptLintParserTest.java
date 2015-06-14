package hudson.plugins.violations.types.zptlint;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.zptlint.ZptlintDescriptor.DESCRIPTOR;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class ZptLintParserTest extends HudsonTestCase {
    @Test
    public void testThatZptLintFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR).reportedIn("**/zptlint-report.log")
                .perform().assertThat("cpplint.py").wasReported()
                .reportedViolation(4796, "C", "abc def ghe '\" 123")
                .reportedViolation(4797, "C", "abc '\" 123 def ghe");
    }
}
