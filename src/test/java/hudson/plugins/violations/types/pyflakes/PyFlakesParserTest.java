package hudson.plugins.violations.types.pyflakes;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.pyflakes.PyflakesDescriptor.DESCRIPTOR;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class PyFlakesParserTest extends HudsonTestCase {
    @Test
    public void testThatPyFlakesFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/pyflakes-report.log")
                .perform()
                .assertThat("cpplint.py")
                .wasReported()
                .reportedViolation(4796, "C",
                        "local variable 'fullname' is assigned to but never used")
                .reportedViolation(4797, "C", "something");
    }
}
