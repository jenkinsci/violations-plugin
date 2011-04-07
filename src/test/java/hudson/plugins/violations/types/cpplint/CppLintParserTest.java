package hudson.plugins.violations.types.cpplint;

import org.junit.Assert;
import org.junit.Test;

public class CppLintParserTest {

	@Test
	public void testParseLineSimple() {
		CppLintParser parser = new CppLintParser();
		CppLintParser.CppLintViolation violation = parser.getCppLintViolation("./tests/writestatusmanagertest.cpp:64:  Missing space after ,  [whitespace/comma] [3]");

		Assert.assertEquals("The message is incorrect", " Missing space after ,  (3)", violation.getMessage());
		Assert.assertEquals("The violation id is incorrect", "whitespace/comma", violation.getViolationId());
		Assert.assertEquals("The line str is incorrect", "64", violation.getLineStr());
		Assert.assertEquals("The file name is incorrect", "./tests/writestatusmanagertest.cpp", violation.getFileName());
	}

}
