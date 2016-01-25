package hudson.plugins.violations.types.pmd;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class PMDDescriptorTest {

    @Test
    public void testMavenTargets() {
        PMDDescriptor pmdDescriptor = new PMDDescriptor();

        List<String> mavenTargets = pmdDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("pmd.xml"));
    }
}
