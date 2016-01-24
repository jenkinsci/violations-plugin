package hudson.plugins.violations.types.codenarc;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CodenarcDescriptorTest {

    @Test
    public void testMavenTargets() {
        CodenarcDescriptor codenarcDescriptor = new CodenarcDescriptor();

        List<String> mavenTargets = codenarcDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("CodeNarcXmlReport.xml"));
    }
}
