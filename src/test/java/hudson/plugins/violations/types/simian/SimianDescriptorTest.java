package hudson.plugins.violations.types.simian;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SimianDescriptorTest {

    @Test
    public void testParserCreation() {
        SimianDescriptor simianDescriptor = new SimianDescriptor();

        assertNotNull(simianDescriptor.createParser());
    }
}
