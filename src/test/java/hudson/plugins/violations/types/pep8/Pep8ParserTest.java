package hudson.plugins.violations.types.pep8;

import static hudson.plugins.violations.ViolationsReportBuilder.violationsReport;
import static hudson.plugins.violations.types.pep8.Pep8Descriptor.DESCRIPTOR;

import org.junit.Assert;
import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

public class Pep8ParserTest extends HudsonTestCase {

    @Test
    public void testParseLineSimple() {
        Pep8Parser parser = new Pep8Parser();
        Pep8Parser.Pep8Violation violation = parser
                .getPep8Violation("trunk/src/python/file.py:1:2: W291 trailing whitespace");

        Assert.assertEquals("The message is incorrect", "trailing whitespace",
                violation.getMessage());
        Assert.assertEquals("The violation id is incorrect", "W291",
                violation.getViolationId());
        Assert.assertEquals("The line str is incorrect", "1",
                violation.getLineStr());
        Assert.assertEquals("The file name is incorrect",
                "trunk/src/python/file.py", violation.getFileName());
    }

    @Test
    public void testParseLineComplexMessage() {
        Pep8Parser parser = new Pep8Parser();
        Pep8Parser.Pep8Violation violation = parser
                .getPep8Violation("trunk/src/python/file.py:98:11: E111 Message #has! 12special-_ chars|?.");

        Assert.assertEquals("The message is incorrect",
                "Message #has! 12special-_ chars|?.", violation.getMessage());
        Assert.assertEquals("The violation id is incorrect", "E111",
                violation.getViolationId());
        Assert.assertEquals("The line str is incorrect", "98",
                violation.getLineStr());
        Assert.assertEquals("The file name is incorrect",
                "trunk/src/python/file.py", violation.getFileName());
    }

    @Test
    public void testThatPep8LogFileCanBeParsed() throws Exception {
        violationsReport(DESCRIPTOR)
                .reportedIn("**/pep8-report.log")
                .perform()
                .assertThat("trunk/src/python/file.py")
                .wasReported()
                .reportedViolation(1, "W291", "trailing whitespace")
                .reportedViolation(98, "E111",
                        "Message #has! 12special-_ chars|?.");
    }
}
