package hudson.plugins.violations.types.pmd;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.pmd.PMDDescriptor.DESCRIPTOR;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class PMDParserTest extends HudsonTestCase {
    @Test
    public void testThatPMDFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/pmd-report.xml")
                .perform()
                .assertThat(
                        "path/to/jenkins/workspace/jobname/module/src/main/java/se/bjurr/PMDAndCheckstyle.java")
                .wasReported()
                .reportedViolation(312, "OverrideBothEqualsAndHashcode",
                        "PMDAndCheckstyle comment in pmd")
                .and()
                .assertThat(
                        "path/to/jenkins/workspace/jobname/module/src/main/java/se/bjurr/PMDFile.java")
                .wasReported()
                .reportedViolation(312, "OverrideBothEqualsAndHashcode",
                        "PMD file comment");
    }
}
