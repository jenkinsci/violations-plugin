package hudson.plugins.violations.types.gendarme;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class GendarmeDescriptorTest {

    @Test
    public void testParserCreation() {
        GendarmeDescriptor gendarmeDescriptor = new GendarmeDescriptor();

        assertNotNull(gendarmeDescriptor.createParser());
    }
}
