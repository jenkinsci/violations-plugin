package hudson.plugins.violations.types.fxcop;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FxCopDescriptorTest {

    @Test
    public void testParserCreation() {
        FxCopDescriptor fxCopDescriptor = new FxCopDescriptor();

        assertNotNull(fxCopDescriptor.createParser());
    }
}
