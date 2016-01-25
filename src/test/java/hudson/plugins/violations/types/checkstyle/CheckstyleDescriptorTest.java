package hudson.plugins.violations.types.checkstyle;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CheckstyleDescriptorTest {

    @Test
    public void testMavenTargets() {
        CheckstyleDescriptor checkstyleDescriptor = new CheckstyleDescriptor();

        List<String> mavenTargets = checkstyleDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("checkstyle-result.xml"));
    }
}
