package hudson.plugins.violations.types.stylecop;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StyleCopDescriptorTest {

    @Test
    public void testParserCreation() {
        StyleCopDescriptor styleCopDescriptor = new StyleCopDescriptor();

        assertNotNull(styleCopDescriptor.createParser());
    }

    @Test
    public void testMavenTargets() {
        StyleCopDescriptor styleCopDescriptor = new StyleCopDescriptor();

        List<String> mavenTargets = styleCopDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("SourceAnalysisViolations.xml"));
    }
}
