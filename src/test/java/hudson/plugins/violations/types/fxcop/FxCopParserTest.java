package hudson.plugins.violations.types.fxcop;

import static org.junit.Assert.*;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.model.FullBuildModel;

import java.io.IOException;

import org.junit.Test;

public class FxCopParserTest extends ViolationsParserTest {
    
    protected FullBuildModel getFullBuildModel(String filename) throws IOException {
    	ViolationsParser parser = new FxCopParser();
    	return getFullBuildModel(parser, filename);
    }
    
    @Test
    public void testParseFullBuildModelFileStringStringArray() throws Exception {
        FullBuildModel model = getFullBuildModel("fxcop.xml");
        
        assertEquals("Number of violations is incorrect", 2, model.getCountNumber("fxcop"));
        assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
    }
}
