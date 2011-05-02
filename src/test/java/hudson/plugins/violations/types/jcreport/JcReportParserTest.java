package hudson.plugins.violations.types.jcreport;

import static org.junit.Assert.assertEquals;
import hudson.plugins.violations.model.FullBuildModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class JcReportParserTest {

    private FullBuildModel getFullBuildModel(String filename) throws IOException {
        URL url = getClass().getResource(filename);
        File xmlFile;
        try {
            xmlFile = new File(url.toURI());
        } catch(URISyntaxException e) {
            xmlFile = new File(url.getPath());
        }

        JcReportParser parser = new JcReportParser();
        FullBuildModel model = new FullBuildModel();
        parser.parse(model, xmlFile.getParentFile(), xmlFile.getName(), null);
        model.cleanup();
        return model;
    }

    @Test
    public void testParseFullBuildModelFileStringStringArray() throws Exception {
        FullBuildModel model = getFullBuildModel("jcoderz-report.xml");

        assertEquals("Number of violations is incorrect", 42, model.getCountNumber("jcreport"));
        assertEquals("Number of files is incorrect", 3, model.getFileModelMap().size());
    }
}
