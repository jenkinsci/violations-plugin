package hudson.plugins.violations.types.simian;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.Violation;

public class SimianParserTest extends ViolationsParserTest {

    protected FullBuildModel getFullBuildModel(String filename) throws IOException {
    	ViolationsParser parser = new SimianParser();
    	return getFullBuildModel(parser, filename);
    }
    
    @Test
    public void testOneFileParsing() throws Exception {
        FullBuildModel model = getFullBuildModel("onefile.xml");
        
        assertEquals("Number of violations is incorrect", 2, model.getCountNumber(SimianParser.TYPE_NAME));
        assertEquals("Number of files is incorrect", 1, model.getFileModelMap().size());
    }
    
    @Test
    public void testOneFilePopupMessage() throws Exception {
        FullBuildModel model = getFullBuildModel("onefile.xml");
        
        Iterator<Violation> iterator = model.getFileModel("java/hudson/maven/MavenBuild.java").getTypeMap().get("simian").descendingIterator();
        Violation v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from line 76.", v.getPopupMessage());
        v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from line 93.", v.getPopupMessage());
    }
    
    @Test
    public void testOneFileMessage() throws Exception {
        FullBuildModel model = getFullBuildModel("onefile.xml");
        
        Iterator<Violation> iterator = model.getFileModel("java/hudson/maven/MavenBuild.java").getTypeMap().get("simian").descendingIterator();
        Violation v = iterator.next();
        assertEquals("Message in violation is incorrect", "Duplication of 6 lines from <a href='#line76'>line 76</a>.", v.getMessage());
        v = iterator.next();
        assertEquals("Message in violation is incorrect", "Duplication of 6 lines from <a href='#line93'>line 93</a>.", v.getMessage());
    }

    @Test
    public void testTwoFileParsing() throws Exception {
        FullBuildModel model = getFullBuildModel("twofile.xml");
        
        assertEquals("Number of violations is incorrect", 2, model.getCountNumber(SimianParser.TYPE_NAME));
        assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
    }
    
    @Test
    public void testTwoFilePopupMessage() throws Exception {
        FullBuildModel model = getFullBuildModel("twofile.xml");
        
        Iterator<Violation> iterator = model.getFileModel("java/hudson/maven/MavenBuild.java").getTypeMap().get(SimianParser.TYPE_NAME).descendingIterator();
        Violation v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from line 61 in MatrixRun.java.", v.getPopupMessage());
        
        iterator = model.getFileModel("java/hudson/matrix/MatrixRun.java").getTypeMap().get(SimianParser.TYPE_NAME).descendingIterator();
        v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from line 92 in MavenBuild.java.", v.getPopupMessage());
    }
    
    @Test
    public void testTwoFileMessage() throws Exception {
        FullBuildModel model = getFullBuildModel("twofile.xml");
        
        Iterator<Violation> iterator = model.getFileModel("java/hudson/maven/MavenBuild.java").getTypeMap().get("simian").descendingIterator();
        Violation v = iterator.next();
        assertEquals("Message in violation is incorrect", "Duplication of 6 lines from <a href='../../matrix/MatrixRun.java#line61'>line 61 in MatrixRun.java</a>.", v.getMessage());

        iterator = model.getFileModel("java/hudson/matrix/MatrixRun.java").getTypeMap().get(SimianParser.TYPE_NAME).descendingIterator();
        v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from <a href='../../maven/MavenBuild.java#line92'>line 92 in MavenBuild.java</a>.", v.getMessage());
    }

    @Test
    public void testTwoSetsParsing() throws Exception {
        FullBuildModel model = getFullBuildModel("twosets.xml");
        
        assertEquals("Number of violations is incorrect", 4, model.getCountNumber(SimianParser.TYPE_NAME));
        assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
    }
    
    @Test
    public void testFourFileMessages() throws Exception {
        FullBuildModel model = getFullBuildModel("fourfile.xml");
        
        Iterator<Violation> iterator = model.getFileModel("java/foo1.java").getTypeMap().get(SimianParser.TYPE_NAME).descendingIterator();
        Violation v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from "
                + "line 21 in foo2.java, "
                + "line 31 in foo3.java "
                + "and line 41 in foo4.java.", v.getPopupMessage());

        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from "
                + "<a href='../foo2.java#line21'>line 21 in foo2.java</a>, "
                + "<a href='../foo3.java#line31'>line 31 in foo3.java</a> and "
                + "<a href='../foo4.java#line41'>line 41 in foo4.java</a>.", v.getMessage());
    }
}
