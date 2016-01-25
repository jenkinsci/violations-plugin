package hudson.plugins.violations.types.resharper;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ReSharperDescriptorTest {

    @Test
    public void testMavenTargets() {
        ReSharperDescriptor reSharperDescriptor = new ReSharperDescriptor();

        List<String> mavenTargets = reSharperDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("resharper.xml"));
    }
}
