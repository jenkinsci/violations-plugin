package hudson.plugins.violations.types.gendarme;

import static org.junit.Assert.assertEquals;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import org.junit.Test;

public class GendarmeParserTest {

	static final Logger logger = Logger.getLogger(GendarmeParserTest.class.toString());
	
	private FullBuildModel getFullBuildModel(String filename) throws IOException {
        URL url = getClass().getResource(filename);
        File xmlFile;
        try {
            xmlFile = new File(url.toURI());
        } catch(URISyntaxException e) {
            xmlFile = new File(url.getPath());
        }
        
        GendarmeParser parser = new GendarmeParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        return model;
    }
	
	@Test
	public void testParseViolationData() throws IOException {
		FullBuildModel model = getFullBuildModel("Gendarme.xml");
		
		assertEquals("Number of violations is incorrect", 3, model.getCountNumber(GendarmeParser.TYPE_NAME));
		for(String fileModelKey : model.getFileModelMap().keySet()){
			FullFileModel ffmodel = model.getFileModelMap().get(fileModelKey);
			logger.info(fileModelKey+".displayName="+ffmodel.getDisplayName());
			logger.info(fileModelKey+".path="+(ffmodel.getSourceFile() == null? "null" : ffmodel.getSourceFile().getAbsolutePath()));
		}
        assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
	}
}
