package hudson.plugins.violations.types.findbugs;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.findbugs.FindBugsDescriptor.DESCRIPTOR;
import jenkins.model.Jenkins;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Jenkins.class })
public class FindBugsParserTest {
    @Test
    public void testThatFindBugsFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/findbugs-report.xml")
                .perform()
                .assertThat("se/bjurr/analyzer/Code.java")
                .wasReported()
                .reportedViolation(8, "EQ_ALWAYS_FALSE",
                        "equals method always returns false");
    }
}
