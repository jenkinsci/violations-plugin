package hudson.plugins.violations.types.resharper;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.resharper.ReSharperDescriptor.DESCRIPTOR;
import jenkins.model.Jenkins;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Jenkins.class })
public class ReSharperParserTest {
    @Test
    public void testThatFindBugsFileCanBeParsed() throws Exception {
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
