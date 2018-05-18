package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

	private String contextArchive;
	private String path;

	@Before
	public void setup() throws IOException {
		contextArchive = "src/test/java/parser/Contexts";
		path = System.getProperty("user.home") + "/archive";
	}

	@Test
	public void testRun() {
		String[] args = { contextArchive, path };
		Parser.main(args);
		File archive = new File(path);
		assertTrue(archive.exists());
		assertTrue(archive.isDirectory());
		File[] files = archive.listFiles();
		assertEquals(files.length, 3);
	}

	@Test
	public void testFailedRun() {
		String[] invalidArgs = { contextArchive };
		Parser.main(invalidArgs);
		File archive = new File(path);
		assertFalse(archive.exists());
	}

	@After
	public void tearDown() {
		File archive = new File(path);
		if (archive.exists() && archive.listFiles() != null) {
			for (File dir : archive.listFiles()) {
				for (File file : dir.listFiles()) {
					file.delete();
				}
				dir.delete();
			}
			archive.delete();
		}
	}
}
