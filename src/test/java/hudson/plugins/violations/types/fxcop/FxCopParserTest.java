package hudson.plugins.violations.types.fxcop;

import static org.junit.Assert.*;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.types.fxcop.FxCopParser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import org.junit.Test;

public class FxCopParserTest {
    
    private FullBuildModel getFullBuildModel(String filename) throws IOException {
        URL url = getClass().getResource(filename);
        File xmlFile;
        try {
            xmlFile = new File(url.toURI());
        } catch(URISyntaxException e) {
            xmlFile = new File(url.getPath());
        }
        
        FxCopParser parser = new FxCopParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        return model;
    }
    
    @Test
    public void testParseFullBuildModelFileStringStringArray() throws Exception {
        FullBuildModel model = getFullBuildModel("fxcop.xml");
        
        assertEquals("Number of violations is incorrect", 2, model.getCountNumber("fxcop"));
        assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
    }
}
