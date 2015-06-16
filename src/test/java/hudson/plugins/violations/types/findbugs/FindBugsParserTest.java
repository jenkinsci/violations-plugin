package hudson.plugins.violations.types.findbugs;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.findbugs.FindBugsDescriptor.FINDBUGS;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class FindBugsParserTest extends HudsonTestCase {
    @Test
    public void testThatFindBugsFileCanBeParsed() throws Exception {
        violationsReport(FINDBUGS).reportedIn("**/findbugs-report.xml").perform()
                .assertThat("se/bjurr/analyzer/Code.java").wasReported()
                .reportedViolation(8, "EQ_ALWAYS_FALSE", "equals method always returns false");
    }
}
