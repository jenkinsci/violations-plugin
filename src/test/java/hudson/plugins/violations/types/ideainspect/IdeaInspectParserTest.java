package hudson.plugins.violations.types.ideainspect;


import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;

import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.Violation;


public class IdeaInspectParserTest extends ViolationsParserTest {

    protected FullBuildModel getFullBuildModel(String filename) throws IOException {
        ViolationsParser parser = new IdeaInspectParser();
        return getFullBuildModel(parser, filename);
    }

    @Test
    public void testExample() throws Exception {
        FullBuildModel results = getFullBuildModel("example.xml");
        assertEquals(3, results.getCountNumber(IdeaInspectParser.TYPE_NAME));

        Set<Violation> lessViolations = results.getFileModel(
            "backend/admin/src/less/main.less").getTypeMap().get(
            IdeaInspectParser.TYPE_NAME);
        assertEquals(2, lessViolations.size());

        Iterator<Violation> lessViolationsIterator = lessViolations.iterator();
        Violation v = lessViolationsIterator.next();
        assertEquals("WARNING", v.getSeverity());
        assertEquals(26, v.getLine());
        assertEquals("Unresolved import", v.getMessage());
        assertEquals("Here be dragons", v.getPopupMessage());

        v = lessViolationsIterator.next();
        assertEquals("ERROR", v.getSeverity());
        assertEquals(131, v.getLine());
        assertEquals("Unresolved import", v.getMessage());
        assertEquals("Description", v.getPopupMessage());

        Set<Violation> pyViolations = results.getFileModel(
            "backend/admin/file.py").getTypeMap().get(
            IdeaInspectParser.TYPE_NAME);
        assertEquals(1, pyViolations.size());

        Iterator<Violation> pyViolationsIterator = pyViolations.iterator();
        v = pyViolationsIterator.next();
        assertEquals("WARNING", v.getSeverity());
        assertEquals(29, v.getLine());
        assertEquals("Unresolved import", v.getMessage());
        assertEquals("Unresolved import", v.getPopupMessage());
    }
}
