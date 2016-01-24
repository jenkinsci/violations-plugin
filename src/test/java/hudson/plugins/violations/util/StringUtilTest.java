package hudson.plugins.violations.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {

    @Test
    public void testRelativePath() {
        String a = "/etc/";
        String b = "/etc/";

        assertEquals("", StringUtil.relativePath(a, b));
    }
}