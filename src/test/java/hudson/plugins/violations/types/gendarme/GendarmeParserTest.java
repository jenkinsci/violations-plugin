package hudson.plugins.violations.types.gendarme;

import static org.junit.Assert.assertEquals;
import hudson.plugins.violations.ViolationsParser;
import hudson.plugins.violations.ViolationsParserTest;
import hudson.plugins.violations.model.FullBuildModel;
import hudson.plugins.violations.model.FullFileModel;
import hudson.plugins.violations.model.Severity;
import hudson.plugins.violations.model.Violation;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;
import java.util.logging.Logger;

import org.junit.Test;
import org.jvnet.hudson.test.Bug;

public class GendarmeParserTest extends ViolationsParserTest {

	static final Logger logger = Logger.getLogger(GendarmeParserTest.class.toString());

	@Override
	protected FullBuildModel getFullBuildModel(String filename) throws IOException {
		ViolationsParser parser = new GendarmeParser();
		return getFullBuildModel(parser, filename);
	}

	@Test
	public void testParseViolationData() throws IOException {
		FullBuildModel model = getFullBuildModel(
				"Gendarme" + (File.separatorChar == '/' ? "_unix" : "") + ".xml");

		assertEquals("Number of violations is incorrect", 3, model.getCountNumber(GendarmeParser.TYPE_NAME));
		for(String fileModelKey : model.getFileModelMap().keySet()){
			FullFileModel ffmodel = model.getFileModelMap().get(fileModelKey);
			logger.info(fileModelKey+".displayName="+ffmodel.getDisplayName());
			logger.info(fileModelKey+".path="+(ffmodel.getSourceFile() == null? "null" : ffmodel.getSourceFile().getAbsolutePath()));
		}
		assertEquals("Number of files is incorrect", 2, model.getFileModelMap().size());
	}

	@Bug(11227)
	@Test
	public void assertThatMultipleDefectsInATargetIsCollected() throws IOException {
		FullBuildModel model = getFullBuildModel("gendarme-2" + (File.separatorChar == '/' ? "_unix" : "") + ".xml");
		assertEquals("Number of violations is incorrect", 12, model.getCountNumber(GendarmeParser.TYPE_NAME));
	}

	@Bug(11227)
	@Test
	public void assertThatSourceFileForTypeDefectsIsAddedFileModel() throws IOException {
		FullBuildModel model = getFullBuildModel("gendarme-2" + (File.separatorChar == '/' ? "_unix" : "") + ".xml");
		assertEquals("Number of files is incorrect", 7, model.getFileModelMap().size());
	}

	@Bug(11227)
	@Test
	public void assertThatCriticalIssuesAreMarkedAsHigh() throws IOException {
		FullBuildModel model = getFullBuildModel("gendarme-2" + (File.separatorChar == '/' ? "_unix" : "") + ".xml");

		SortedSet<Violation> set = model.getFileModel(getOsDependentFilename("workspaceLeave\\Leave.Gui\\Views\\LeaveGanttView\\Column\\LeaveFooter.cs")).getTypeMap().get(GendarmeParser.TYPE_NAME);
		assertEquals("The severity is incorrect", Severity.HIGH, set.first().getSeverity());
	}

	private static String getOsDependentFilename(String windowsFilename) {
		return (File.separatorChar == '\\' ? windowsFilename : windowsFilename.replace('\\', File.separatorChar));
	}
}
