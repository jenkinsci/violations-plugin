package hudson.plugins.violations.types.resharper;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.resharper.ReSharperDescriptor.DESCRIPTOR;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class ReSharperParserTest extends HudsonTestCase {
    @Test
    public void testThatResharperFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/resharper-report.xml")
                .perform()
                .assertThat("MyLibrary/Class1.cs")
                .wasReported()
                .reportedViolation(0, "Redundancies in Code",
                        "Using directive is not required by the code and can be safely removed")
                .reportedViolation(9, "Common Practices and Code Improvements",
                        "Join declaration and assignment")
                .and()
                .assertThat("MyLibrary/Properties/AssemblyInfo.cs")
                .wasReported()
                .reportedViolation(2, "Redundancies in Code",
                        "Using directive is not required by the code and can be safely removed");
    }
}
