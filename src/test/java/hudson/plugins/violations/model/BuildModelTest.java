package hudson.plugins.violations.model;

import static org.junit.Assert.assertEquals;
import hudson.plugins.violations.model.BuildModel.TypeCount;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class BuildModelTest {
    @Test
    public void testThatNameCanBeUrlEncoded() throws UnsupportedEncodingException {
        TypeCount typeCount = new BuildModel.TypeCount("a/name\\with/slashes", 1, 1);
        assertEquals("a/name\\with/slashes", typeCount.getName());
        assertEquals("a%2Fname%5Cwith%2Fslashes", typeCount.getUrlEncodedName());
    }
}
