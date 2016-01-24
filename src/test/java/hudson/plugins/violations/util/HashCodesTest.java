package hudson.plugins.violations.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashCodesTest {

    @Test
    public void testHashCode() {
        String a = "This is a String";
        int hash = a.hashCode();

        int expected = 31 * 17 + hash;
        assertEquals(expected, HashCodes.hashCode(a));

        expected = 31 * (31 * 17 + hash) + hash;
        assertEquals(expected, HashCodes.hashCode(a, a));
    }
}
