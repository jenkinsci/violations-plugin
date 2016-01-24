package hudson.plugins.violations.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualsTest {

    @Test
    public void testEquals() {
        String a = "First";
        String b = "Second";
        String c = "First";

        assertTrue(Equals.equals(null, null));
        assertFalse(Equals.equals(null, b));
        assertEquals(a.equals(b), Equals.equals(a, b));
        assertEquals(a.equals(c), Equals.equals(a, c));
    }

    @Test
    public void testEqualsMultiple() {
        String a = "First";
        String b = "Second";
        String c = "First";

        assertTrue(Equals.equals(null, null, null));
        assertFalse(Equals.equals(null, b, c));
        assertTrue(Equals.equals(a, a, c));
        assertFalse(Equals.equals(a, b, c));
    }
}
