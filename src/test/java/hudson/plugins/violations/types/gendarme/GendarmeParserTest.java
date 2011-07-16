package hudson.plugins.violations.types.gendarme;

import static org.junit.Assert.*;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

public class GendarmeParserTest extends ViolationsParserTest {

	static final Logger logger = Logger.getLogger(GendarmeParserTest.class.toString());
	
    protected FullBuildModel getFullBuildModel(String filename) throws IOException {
    	ViolationsParser parser = new GendarmeParser();
    	return getFullBuildModel(parser, filename);
    }
	
	@Test
	public void testParseViolationData() throws IOException {
		FullBuildModel model = getFullBuildModel(
                        "Gendarme" + (File.separatorChar == '/' ? "_unix" : "") + ".xml");
		
		assertEquals("Number of violations is incorrect", 3, model.getCountNumber(GendarmeParser.TYPE_NAME));
		for(String fileModelKey : model.getFileModelMap().keySet()){
			FullFileModel ffmodel = model.getFileModelMap().get(fileModelKey);
			logger.info(fileModelKey+".displayName="+ffmodel.getDisplayName());
			logger.info(fileModelKey+".path="+(ffmodel.getSourceFile() == null? "null" : ffmodel.getSourceFile().getAbsolutePath()));
		}
        assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
	}
}
