package hudson.plugins.violations.types.findbugs;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class FindBugsDescriptorTest {

    @Test
    public void testMavenTargets() {
        FindBugsDescriptor findBugsDescriptor = new FindBugsDescriptor();

        List<String> mavenTargets = findBugsDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("findbugsXml.xml"));
        assertTrue(mavenTargets.contains("findbugs.xml"));
    }
}
