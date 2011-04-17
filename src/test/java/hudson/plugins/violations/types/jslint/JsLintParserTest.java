package hudson.plugins.violations.types.jslint;

import static org.junit.Assert.*;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;
import hudson.plugins.violations.types.jslint.JsLintParser;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

public class JsLintParserTest extends ViolationsParserTest {
    
    protected FullBuildModel getFullBuildModel(String filename) throws IOException {
    	ViolationsParser parser = new JsLintParser();
    	return getFullBuildModel(parser, filename);
    }

    @Test
    public void testParseWithSingleFile() throws Exception {
        FullBuildModel model = getFullBuildModel("single.xml");

        // check number of violations and number of files
        assertEquals(51, model.getCountNumber(JsLintParser.TYPE_NAME));
        assertEquals(1, model.getFileModelMap().size());
        
        assertPrototype(model);
    }
    
    @Test
    public void testParseWithMultipleFile() throws Exception {
        FullBuildModel model = getFullBuildModel("multi.xml");

        assertEquals(102, model.getCountNumber(JsLintParser.TYPE_NAME));
        assertEquals(2, model.getFileModelMap().size());
        
        assertScriptaculous(model);
        assertPrototype(model);
    }
    
    private void assertPrototype(FullBuildModel model) {
        Iterator<Violation> iterator = model.getFileModel("duckworth/hudson-jslint-freestyle/src/prototype.js").getTypeMap().get(JsLintParser.TYPE_NAME).iterator();
        
        // check the first two violations
        Violation v = iterator.next();
        assertEquals("Expected 'Version' to have an indentation at 5 instead at 3.", v.getPopupMessage());
        assertEquals(10, v.getLine());
        assertEquals(Severity.MEDIUM, v.getSeverity());
        assertEquals("1',", v.getSource());
        v = iterator.next();
        assertEquals("Expected 'Browser' to have an indentation at 5 instead at 3.", v.getPopupMessage());
        assertEquals(12, v.getLine());
        assertEquals(Severity.MEDIUM, v.getSeverity());
        assertEquals("  Browser: (function(){", v.getSource());
        
        // check the last violation
        while (iterator.hasNext()) {
            v = iterator.next();
        }
        
        assertEquals("Too many errors. (0% scanned).", v.getPopupMessage());
        assertEquals(46, v.getLine());
        assertEquals(Severity.MEDIUM, v.getSeverity());
        assertEquals("", v.getSource());
    }
    
    private void assertScriptaculous(FullBuildModel model) {
        Iterator<Violation> iterator = model.getFileModel("duckworth/hudson-jslint-freestyle/src/scriptaculous.js").getTypeMap().get(JsLintParser.TYPE_NAME).iterator();
        
        // check the first two violations
        Violation v = iterator.next();
        assertEquals("Expected 'Version' to have an indentation at 5 instead at 3.", v.getPopupMessage());
        assertEquals(27, v.getLine());
        assertEquals(Severity.MEDIUM, v.getSeverity());
        assertEquals("3',", v.getSource());
        v = iterator.next();
        assertEquals("Expected 'require' to have an indentation at 5 instead at 3.", v.getPopupMessage());
        assertEquals(28, v.getLine());
        assertEquals(Severity.MEDIUM, v.getSeverity());
        assertEquals("  require: function(libraryName) {", v.getSource());
        
        // check the last violation
        while (iterator.hasNext()) {
            v = iterator.next();
        }
        
        assertEquals("Too many errors. (83% scanned).", v.getPopupMessage());
        assertEquals(57, v.getLine());
        assertEquals(Severity.MEDIUM, v.getSeverity());
        assertEquals("", v.getSource());
    }
}