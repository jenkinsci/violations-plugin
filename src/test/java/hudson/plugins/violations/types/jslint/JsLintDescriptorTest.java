package hudson.plugins.violations.types.jslint;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsLintDescriptorTest {

    @Test
    public void testParserCreation() {
        JsLintDescriptor jsLintDescriptor = new JsLintDescriptor();

        assertNotNull(jsLintDescriptor.createParser());
    }

    @Test
    public void testMavenTargets() {
        JsLintDescriptor jsLintDescriptor = new JsLintDescriptor();

        List<String> mavenTargets = jsLintDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("jslint.xml"));
    }
}
