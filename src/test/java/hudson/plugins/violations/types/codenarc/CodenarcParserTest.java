package hudson.plugins.violations.types.codenarc;

import static org.junit.Assert.*;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.model.FullBuildModel;

import java.io.IOException;

import hudson.plugins.violations.model.FullFileModel;
import org.junit.Test;

/**
 * Test codenarc against a test resource file.
 *
 * @author Robin Bramley, Opsera Ltd.
 */
public class CodenarcParserTest extends ViolationsParserTest {
    
    protected FullBuildModel getFullBuildModel(String filename) throws IOException {
    	ViolationsParser parser = new CodenarcParser();
    	return getFullBuildModel(parser, filename);
    }
    
    @Test
    public void testParseFullBuildModelFromFile() throws Exception {
        FullBuildModel model = getFullBuildModel("CodeNarcXmlReport.xml");
        FullFileModel fileModel = model.getFileModel("grails-app/controllers/LoginController.groovy");

        assertEquals("Number of violations is incorrect", 10, model.getCountNumber("codenarc"));
        assertEquals("Number of files is incorrect", 7, model.getFileModelMap().size());
        assertNotNull("LoginController model is null", fileModel.getSourceFile());
    }

    @Test
    public void testParseFullBuildModelFromFileWithSourceDirectory() throws Exception {
        FullBuildModel model = getFullBuildModel("CodeNarcXmlReportWithSourceDirectory.xml");
        FullFileModel fileModel = model.getFileModel("webapps/testapp/grails-app/controllers/LoginController.groovy");

        assertEquals("Number of violations is incorrect", 10, model.getCountNumber("codenarc"));
        assertEquals("Number of files is incorrect", 7, model.getFileModelMap().size());
        assertNotNull("LoginController model is null", fileModel.getSourceFile());

    }

    @Test
    public void testParser2() throws Exception {
        FullBuildModel model = getFullBuildModel("CodeNarcReport2.xml");
        
        assertEquals("Number of violations is incorrect", 11, model.getCountNumber("codenarc"));
        assertEquals("Number of files is incorrect", 1, model.getFileModelMap().size());
    }
}
