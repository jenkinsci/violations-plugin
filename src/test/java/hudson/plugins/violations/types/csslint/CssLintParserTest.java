package hudson.plugins.violations.types.csslint;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.csslint.CssLintDescriptor.DESCRIPTOR;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class CssLintParserTest extends HudsonTestCase {
    @Test
    public void testThatCsslintFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/lint-report.xml, **/csslint-report.xml")
                .perform().assertThat("some/filename.css").wasReported()
                .reportedViolation(1, "ALSO BOGUS", "BOGUS");
    }
}
