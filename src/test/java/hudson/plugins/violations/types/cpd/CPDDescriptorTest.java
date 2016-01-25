package hudson.plugins.violations.types.cpd;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CPDDescriptorTest {

    @Test
    public void testMavenTargets() {
        CPDDescriptor cpdDescriptor = new CPDDescriptor();

        List<String> mavenTargets = cpdDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("cpd.xml"));
    }
}
