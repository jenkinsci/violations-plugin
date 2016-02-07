package hudson.plugins.violations.model;

import static org.junit.Assert.*;

import org.junit.Test;

import hudson.plugins.violations.util.Equals;

public class SuppressionTest {

	@Test
	public void equalsTest() {
		Suppression s =new Suppression("type", "source", "filename", "reason", "message");
		Suppression s1 = null;

		assertFalse(s.equals(null));;
		assertFalse(s.equals(s1));


		s1 = new Suppression("otherType", "source", "filename", "reason", "message");

		assertFalse(s.equals(s1));
		assertFalse(s1.equals(s));

		s1 = new Suppression("type", "otherSource", "filename", "reason", "message");

		assertFalse(s.equals(s1));
		assertFalse(s1.equals(s));

		s1 = new Suppression("type", "source", "otherFilename", "reason", "message");

		assertFalse(s.equals(s1));
		assertFalse(s1.equals(s));

		s1 = new Suppression("type", "source", "filename", "otherReason", "message");

		assertTrue(s.equals(s1));
		assertTrue(s1.equals(s));

		s1 = new Suppression("type", "source", "filename", "reason", "otherMessage");

		assertTrue(s.equals(s1));
		assertTrue(s1.equals(s));
		
		s1 = new Suppression("type", "source", "filename", "reason", "message");
		assertTrue(s1.equals(s));
		assertTrue(s.equals(s1));


	}

	@Test
	public void hashcodeTest() {
		Suppression s = new Suppression("type", "source", "filename", "reason", "message");
		Suppression s1 = new Suppression("type", "source", "filename", "reason", "message");
		assertTrue(s.hashCode() == s1.hashCode());
		
		
		s1 = new Suppression("otherType", "source", "filename", "reason", "message");

		assertFalse(s.hashCode() == s1.hashCode());

		s1 = new Suppression("type", "otherSource", "filename", "reason", "message");

		assertFalse(s.hashCode() == s1.hashCode());

		s1 = new Suppression("type", "source", "otherFilename", "reason", "message");

		assertFalse(s.hashCode() == s1.hashCode());


	}

}
