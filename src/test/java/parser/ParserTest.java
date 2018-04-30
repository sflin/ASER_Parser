package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import examples.Parser;

public class ParserTest {
	
	private String contextArchive;
	
	@Before
	public void setup() {
		contextArchive = "src/test/java/parser/test-contexts.zip";
	}
	
	@Test
	public void testRun() {
		String[] args = {contextArchive};
		Parser.main(args);
		File archive = new File("/home/selin/archive");
		assertTrue(archive.exists());
		assertTrue(archive.isDirectory());
		File[] files = archive.listFiles();
		assertEquals(files.length, 4);
	}
	
	@After
	public void tearDown() {
		File archive = new File("/home/selin/archive");
		for(File dir : archive.listFiles()) {
			for(File file : dir.listFiles()) {
				file.delete();
			}
			dir.delete();
		}
		archive.delete();
	}

}
