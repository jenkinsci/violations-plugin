package hudson.plugins.violations;

import hudson.plugins.violations.model.FullBuildModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public abstract class ViolationsParserTest {
	
	protected abstract FullBuildModel getFullBuildModel(String filename) throws IOException;
	
	protected FullBuildModel getFullBuildModel(ViolationsParser parser, String filename) throws IOException {
		URL url = getClass().getResource(filename);
        File xmlFile;
        try {
            xmlFile = new File(url.toURI());
        } catch(URISyntaxException e) {
            xmlFile = new File(url.getPath());
        }
        
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), new String[0]);
        model.cleanup();
        return model;
	}
}
