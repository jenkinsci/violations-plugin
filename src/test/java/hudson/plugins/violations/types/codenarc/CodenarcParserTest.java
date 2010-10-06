package hudson.plugins.violations.types.codenarc;

import static org.junit.Assert.*;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.types.codenarc.CodenarcParser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import org.junit.Test;

/**
 * Test codenarc against a test resource file.
 *
 * @author Robin Bramley, Opsera Ltd.
 */
public class CodenarcParserTest {
    
    private FullBuildModel getFullBuildModel(String filename) throws IOException {
        URL url = getClass().getResource(filename);
        File xmlFile;
        try {
            xmlFile = new File(url.toURI());
        } catch(URISyntaxException e) {
            xmlFile = new File(url.getPath());
        }
        
        CodenarcParser parser = new CodenarcParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        return model;
    }
    
    @Test
    public void testParseFullBuildModelFromFile() throws Exception {
        FullBuildModel model = getFullBuildModel("CodeNarcXmlReport.xml");
        
        assertEquals("Number of violations is incorrect", 10, model.getCountNumber("codenarc"));
        assertEquals("Number of files is incorrect", 7, model.getFileModelMap().size());
    }

}
