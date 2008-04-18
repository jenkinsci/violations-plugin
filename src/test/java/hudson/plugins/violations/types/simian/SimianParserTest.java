package hudson.plugins.violations.types.simian;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;

import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.Violation;

public class SimianParserTest {
    
    @Test
    public void testOneFileParsing() throws Exception {
        File xmlFile = new File( getClass().getResource("onefile.xml").getFile());

        SimianParser parser = new SimianParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        
        assertEquals("Number of violations is incorrect", 2, model.getCountNumber(SimianParser.TYPE_NAME));
        assertEquals("Number of files is incorrect", 1, model.getFileModelMap().size());
    }
    
    @Test
    public void testOneFilePopupMessage() throws Exception {
        File xmlFile = new File( getClass().getResource("onefile.xml").getFile());
        SimianParser parser = new SimianParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        
        Iterator<Violation> iterator = model.getFileModel("java/hudson/maven/MavenBuild.java").getTypeMap().get("simian").descendingIterator();
        Violation v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from line 76", v.getPopupMessage());
        v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from line 93", v.getPopupMessage());
    }
    
    @Test
    public void testOneFileMessage() throws Exception {
        File xmlFile = new File( getClass().getResource("onefile.xml").getFile());
        SimianParser parser = new SimianParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        
        Iterator<Violation> iterator = model.getFileModel("java/hudson/maven/MavenBuild.java").getTypeMap().get("simian").descendingIterator();
        Violation v = iterator.next();
        assertEquals("Message in violation is incorrect", "Duplication of 6 lines from <a href='#line76'>line 76</a>", v.getMessage());
        v = iterator.next();
        assertEquals("Message in violation is incorrect", "Duplication of 6 lines from <a href='#line93'>line 93</a>", v.getMessage());
    }

    @Test
    public void testTwoFileParsing() throws Exception {
        File xmlFile = new File( getClass().getResource("twofile.xml").getFile());

        SimianParser parser = new SimianParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        
        assertEquals("Number of violations is incorrect", 2, model.getCountNumber(SimianParser.TYPE_NAME));
        assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
    }
    
    @Test
    public void testTwoFilePopupMessage() throws Exception {
        File xmlFile = new File( getClass().getResource("twofile.xml").getFile());
        SimianParser parser = new SimianParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        
        Iterator<Violation> iterator = model.getFileModel("java/hudson/maven/MavenBuild.java").getTypeMap().get(SimianParser.TYPE_NAME).descendingIterator();
        Violation v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from line 61 in MatrixRun.java", v.getPopupMessage());
        
        iterator = model.getFileModel("java/hudson/matrix/MatrixRun.java").getTypeMap().get(SimianParser.TYPE_NAME).descendingIterator();
        v = iterator.next();
        assertEquals("Popup message in violation is incorrect", "Duplication of 6 lines from line 92 in MavenBuild.java", v.getPopupMessage());
    }

    @Test
    public void testTwoSetsParsing() throws Exception {
        File xmlFile = new File( getClass().getResource("twosets.xml").getFile());

        SimianParser parser = new SimianParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        
        assertEquals("Number of violations is incorrect", 4, model.getCountNumber(SimianParser.TYPE_NAME));
        assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
    }
}
