package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;

import examples.Parser;

public class ParserTest {
	
	private String contextArchive;
	
	@Before
	public void setup() {
		contextArchive = "";
	}
	
	public void testRun() {
		Parser.run(contextArchive);
		File archive = new File("/home/selin/archive");
		assertTrue(archive.exists());
		assertTrue(archive.isDirectory());
		File[] files = archive.listFiles();
		assertEquals(files.length, 10);
	}

}
