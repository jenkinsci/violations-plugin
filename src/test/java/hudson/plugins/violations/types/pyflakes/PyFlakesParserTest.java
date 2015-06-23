package hudson.plugins.violations.types.pyflakes;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.pyflakes.PyflakesDescriptor.PYFLAKES;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

public class PyFlakesParserTest extends JenkinsRule {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testThatPyFlakesFileCanBeParsed() throws Exception {
        violationsReport(PYFLAKES).reportedIn("**/pyflakes-report.log").perform().assertThat("cpplint.py")
                .wasReported().reportedViolation(4796, "C", "local variable 'fullname' is assigned to but never used")
                .reportedViolation(4797, "C", "something");
    }
}
