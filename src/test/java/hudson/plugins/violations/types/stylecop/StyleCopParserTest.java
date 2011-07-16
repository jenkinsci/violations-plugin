package hudson.plugins.violations.types.stylecop;

import static org.junit.Assert.*;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.types.stylecop.StyleCopParser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

public class StyleCopParserTest extends ViolationsParserTest {
    
    protected FullBuildModel getFullBuildModel(String filename) throws IOException {
    	ViolationsParser parser = new StyleCopParser();
    	return getFullBuildModel(parser, filename);
    }
    
    @Test
    public void testParseFullBuildModelFileStringStringArray() throws Exception {
        FullBuildModel model = getFullBuildModel("onefile.xml");
        
        assertEquals("Number of violations is incorrect", 3, model.getCountNumber(StyleCopParser.TYPE_NAME));
        assertEquals("Number of files is incorrect", 1, model.getFileModelMap().size());
    }

    @Test
    public void testParseViolationData() throws Exception {
        FullBuildModel model = getFullBuildModel("onefile.xml");
        Iterator<Violation> iterator = model.getFileModel("MainClass.cs").getTypeMap().get(StyleCopParser.TYPE_NAME).descendingIterator();
        Violation v = iterator.next();
        assertEquals("Line in violation is incorrect", 10, v.getLine());
        assertEquals("Source in violation is incorrect", "DocumentationRules", v.getSource());
        assertEquals("Message in violation is incorrect", "The property must have a documentation header. (SA1600)", v.getMessage());
        assertEquals("Severity level in violation is incorrect", 2, v.getSeverityLevel());
        assertEquals("Severity in violation is incorrect", "Medium", v.getSeverity());
    }
    
    /**
     * Test to catch a NPE in Violation.compareTo() because violation has to little data
     */
    @Test
    public void testViolationCompareTo() throws Exception {
        FullBuildModel model = getFullBuildModel("onefile.xml");
        Iterator<Violation> iterator = model.getFileModel("MainClass.cs").getTypeMap().get(StyleCopParser.TYPE_NAME).descendingIterator();
        Violation v = iterator.next();
        Violation otherV = iterator.next();
        assertTrue("compareTo() should return false", v.compareTo(otherV) != 0);
    }
    
    @Test
    public void assertParsingVersion43() throws Exception {
        FullBuildModel model = getFullBuildModel("stylecop-v4.3.xml");
        
        assertEquals("Number of violations is incorrect", 2, model.getCountNumber(StyleCopParser.TYPE_NAME));
        assertEquals("Number of files is incorrect", 1, model.getFileModelMap().size());
    }
}
