package hudson.plugins.violations.types.cpplint;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.cpplint.CppLintDescriptor.CPPLINT;

import org.junit.Assert;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class CppLintParserTest extends HudsonTestCase {

    @Test
    public void testParseLineSimple() {
        CppLintParser parser = new CppLintParser();
        CppLintParser.CppLintViolation violation = parser
                .getCppLintViolation("./tests/writestatusmanagertest.cpp:64:  Missing space after ,  [whitespace/comma] [3]");

        Assert.assertEquals("The message is incorrect", " Missing space after ,  (3)", violation.getMessage());
        Assert.assertEquals("The violation id is incorrect", "whitespace/comma", violation.getViolationId());
        Assert.assertEquals("The line str is incorrect", "64", violation.getLineStr());
        Assert.assertEquals("The file name is incorrect", "./tests/writestatusmanagertest.cpp", violation.getFileName());
    }

    @Test
    public void testThatCpplintFileCanBeParsed() throws Exception {
        violationsReport(CPPLINT)
                .reportedIn("**/cpplint-report.txt")
                .perform()
                .assertThat("hello.cpp")
                .wasReported()
                .reportedViolation(0, "legal/copyright",
                        "No copyright message found.  You should have a line: \"Copyright [year] <Copyright Owner>\"  (5)")
                .reportedViolation(5, "whitespace/braces",
                        "{ should almost always be at the end of the previous line  (4)");
    }
}
