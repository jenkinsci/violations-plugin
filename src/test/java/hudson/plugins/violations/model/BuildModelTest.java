package hudson.plugins.violations.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import hudson.plugins.violations.MagicNames;
import hudson.plugins.violations.model.BuildModel.FileCount;
import hudson.plugins.violations.model.BuildModel.TypeCount;
import hudson.plugins.violations.render.FileModelProxy;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class BuildModelTest {
	@Test
	public void testThatNameCanBeUrlEncoded() throws UnsupportedEncodingException {
		TypeCount typeCount = new BuildModel.TypeCount("a/name\\with/slashes", 1, 1);
		assertEquals("a/name\\with/slashes", typeCount.getName());
		assertEquals("a%2Fname%5Cwith%2Fslashes", typeCount.getUrlEncodedName());
	}

	@Test
	public void testGetTypeCounts(){
		File xmlFile = new File( MagicNames.VIOLATIONS + "/" + MagicNames.VIOLATIONS + ".xml");
		BuildModel  model = new BuildModel(xmlFile);
		assertEquals(model.getTypeCounts().size(), 0);

		model.addFileCount("type", "name",new int[] {1,2,3});

		assertEquals(model.getTypeCounts().size(), 1);


	}

	@Test
	public void testaddFileCount(){
		File xmlFile = new File( MagicNames.VIOLATIONS + "/" + MagicNames.VIOLATIONS + ".xml");
		BuildModel  model = new BuildModel(xmlFile);
		assertEquals(model.getFileCounts("type").size(), 0);

		model.addFileCount("type", "name",new int[] {1,2,3});

		assertEquals(model.getFileCounts("type").size(), 1);

		model.addFileCount("type", "name2",new int[] {1,2,3});

		assertEquals(model.getFileCounts("type").size(), 2);

	}


	@Test
	public void testCompareTo(){

		FileCount f = new FileCount("name", new int[] {1,2,3}, null);


		//this == other
		assertEquals(f.compareTo(f), 0);

		FileCount f1 = new FileCount("name", new int[] {2,2,3}, null);

		//totalcount < other.totalCount
		assertEquals(f.compareTo(f1),1);


		f1 = new FileCount("name", new int[] {0,2,3}, null);
		//totalcount > other.totalCount
		assertEquals(f.compareTo(f1),-1);


		f1 = new FileCount("name", new int[] {1,2,3}, null);

		//name == other.name
		assertEquals(f.compareTo(f1),0);

		f1 = new FileCount("name2", new int[] {1,2,3}, null);

		//name != other.name
		assertEquals(f.compareTo(f1),-1);

	}
}

