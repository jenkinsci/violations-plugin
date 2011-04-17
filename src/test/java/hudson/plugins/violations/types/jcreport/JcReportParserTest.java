package hudson.plugins.violations.types.jcreport;

import static org.junit.Assert.*;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.model.FullBuildModel;

import java.io.IOException;

import org.junit.Test;

public class JcReportParserTest extends ViolationsParserTest {
    
    protected FullBuildModel getFullBuildModel(String filename) throws IOException {
    	ViolationsParser parser = new JcReportParser();
    	return getFullBuildModel(parser, filename);
    }
    
    @Test
    public void testParseFullBuildModelFileStringStringArray() throws Exception {
        FullBuildModel model = getFullBuildModel("jcoderz-report.xml");
        
        assertEquals("Number of violations is incorrect", 42, model.getCountNumber("jcreport"));
        assertEquals("Number of files is incorrect", 3, model.getFileModelMap().size());
    }
}
