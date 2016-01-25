package hudson.plugins.violations.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComparesTest {

    @Test
    public void testCompare() {
        String a = "First";
        String b = "Second";

        assertEquals(0, Compares.compare(null, null));
        assertEquals(-1, Compares.compare(null, b));
        assertEquals(1, Compares.compare(a, null));

        assertEquals(a.compareTo(b), Compares.compare(a, b));
    }
}
