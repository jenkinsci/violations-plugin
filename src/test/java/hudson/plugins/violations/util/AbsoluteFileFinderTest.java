package hudson.plugins.violations.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AbsoluteFileFinderTest {

    @Test
    public void testFileForName() {
        AbsoluteFileFinder absoluteFileFinder = new AbsoluteFileFinder();
        absoluteFileFinder.addSourcePath("src/test/resources");

        assertNotNull(absoluteFileFinder.getFileForName("rootDir.txt"));
        assertNull(absoluteFileFinder.getFileForName("doesntexistfile.txt"));
    }
}
