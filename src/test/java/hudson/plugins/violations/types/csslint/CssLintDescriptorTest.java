package hudson.plugins.violations.types.csslint;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CssLintDescriptorTest {

    @Test
    public void testMavenTargets() {
        CssLintDescriptor cssLintDescriptor = new CssLintDescriptor();

        List<String> mavenTargets = cssLintDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("csslint.xml"));
    }
}
