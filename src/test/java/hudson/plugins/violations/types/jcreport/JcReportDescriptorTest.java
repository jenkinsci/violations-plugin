package hudson.plugins.violations.types.jcreport;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JcReportDescriptorTest {

    @Test
    public void testParserCreation() {
        JcReportDescriptor jcReportDescriptor = new JcReportDescriptor();

        assertNotNull(jcReportDescriptor.createParser());
    }

    @Test
    public void testMavenTargets() {
        JcReportDescriptor jcReportDescriptor = new JcReportDescriptor();

        List<String> mavenTargets = jcReportDescriptor.getMavenTargets();

        assertTrue(mavenTargets.contains("jcoderz-report.xml"));
    }
}
