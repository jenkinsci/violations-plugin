package hudson.plugins.violations.types.cpd;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.cpd.CPDDescriptor.DESCRIPTOR;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class CPDParserTest extends HudsonTestCase {

    @Test
    public void testThatCpdFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/cpd-report.xml")
                .perform()
                .assertThat(
                        "home/goetas/gits/webservices/src/goetas/webservices/bindings/soap12/transport/http/Http.php")
                .wasReported()
                .reportedViolation(
                        36,
                        "duplication",
                        "Duplication of 38 tokens from <a href='../../../../soap/transport/http/Http.php#line41'>line 41 in Http.php</a>")
                .and()
                .assertThat(
                        "home/goetas/gits/webservices/src/goetas/webservices/bindings/soap/transport/http/Http.php")
                .wasReported()
                .reportedViolation(
                        41,
                        "duplication",
                        "Duplication of 38 tokens from <a href='../../../../soap12/transport/http/Http.php#line36'>line 36 in Http.php</a>");
    }
}
