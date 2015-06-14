package hudson.plugins.violations.types.pylint;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.xmllint.XmllintDescriptor.DESCRIPTOR;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class XmlLintParserTest extends HudsonTestCase {
    @Test
    public void testThatXmllintFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/xmllint-report.log")
                .perform()
                .assertThat("xmlfile.xml")
                .wasReported()
                .reportedViolation(3, "C",
                        "parser error : AttValue: ' expected")
                .reportedViolation(3, "C",
                        "parser error : Couldn't find end of Start Tag head line 2");
    }
}
