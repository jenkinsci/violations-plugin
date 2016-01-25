package hudson.plugins.violations.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HealthNumberTest {

    @Test
    public void testCalculate() {
        HealthNumber healthNumber = new HealthNumber(0, 100);

        assertEquals(100, healthNumber.calculate(-1));
        assertEquals(33, healthNumber.calculate(80));
        assertEquals(0, healthNumber.calculate(112));

        healthNumber = new HealthNumber(10, 80);
        assertEquals(93, healthNumber.calculate(4));
    }
}
