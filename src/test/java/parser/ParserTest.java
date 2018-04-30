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
	private String path;
	
	@Before
	public void setup() {
		contextArchive = "src/test/java/parser/test-contexts.zip";
		path = System.getProperty("user.home") + "/archive";
	}
	
	@Test
	public void testRun() {
		String[] args = {contextArchive};
		Parser.main(args);
		File archive = new File(path);
		assertTrue(archive.exists());
		assertTrue(archive.isDirectory());
		File[] files = archive.listFiles();
		assertEquals(files.length, 3);
	}
	
	@After
	public void tearDown() {
		File archive = new File(path);
		for(File dir : archive.listFiles()) {
			for(File file : dir.listFiles()) {
				file.delete();
			}
			dir.delete();
		}
		archive.delete();
	}

}
