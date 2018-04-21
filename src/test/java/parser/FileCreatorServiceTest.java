package parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import parser.model.ClassCollection;
import parser.model.MethodCollection;

public class FileCreatorServiceTest {
	
	public MethodName method;
	public MethodName constructor;
	public ClassCollection classCollection;
	public FileCreatorService fcs;
	
	@Before
	public void setup() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		fcs = new FileCreatorService("src/test/java/parser/testarchive");
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(new File("src/test/java/parser/TestFile.json")), "UTF-8")) {
			classCollection = new Gson().fromJson(reader, ClassCollection.class);
		}
		method = new MethodName("0M:static [p:string] [p:string].Format([p:string] format, [p:object] arg0, [p:object] arg1)");
		constructor = new MethodName("0M:[p:void] [System.Runtime.Serialization.Formatters.Binary.BinaryFormatter, mscorlib, 4.0.0.0]..ctor()");
	}

	@Test
	public void testAddMethod() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		fcs.addMethod(method);
		File testFile = new File("src/test/java/parser/testarchive/mscorlib/string.json");
		assertTrue(testFile.exists());
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			String actualName = testCollection.getCollections().get(0).getMethods().get(0).getName();
			String expectedName = "System.String.Format(System.String format, System.Object arg0, System.Object arg1)";
			assertEquals(expectedName, actualName);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(1), actualCount);
		}
		fcs.addMethod(method);
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(2), actualCount);
		}
	}
	
	@Test
	public void testGetMethodName() {
		String methodName = fcs.getMethodName(method);
		String expectedName = "System.String.Format(System.String format, System.Object arg0, System.Object arg1)";
		assertEquals(expectedName, methodName);
		methodName = fcs.getMethodName(constructor);
		expectedName = "System.Runtime.Serialization.Formatters.Binary.BinaryFormatter()";
		assertEquals(expectedName, methodName);
	}
	
	@Test
	public void testGetClassCollection() { 
		ClassCollection testClassCollection = fcs.getClassCollection(new File("src/test/java/parser/TestFile.json"));
		assertNotNull(testClassCollection);
		assertEquals(testClassCollection.getClass(), ClassCollection.class);
//		assertThrows(FileNotFoundException.class, () -> fcs.getClassCollection(new File("src/test/java/parser/NotExistingFile.json")));
//		assertThrows(IOException.class, () -> fcs.getClassCollection(new File("src/test/java/parser/TestContext.json")));
	}

	@Test
	public void testGetMethodCollectionByClass() {
		MethodCollection testCollection = fcs.getMethodCollectionByClass("System.String", classCollection);
		assertEquals(testCollection, classCollection.getCollections().get(0));
		MethodCollection nullCollection = fcs.getMethodCollectionByClass("notExistingMethod", classCollection);
		assertNull(nullCollection);
	}
	
	@Test
	public void testWriteCollectionToFile() {
		ClassCollection collection = new ClassCollection(null);
		File f = new File("src/test/java/parser/Output.json");
		fcs.writeCollectionToFile(collection, f);
		assertTrue(f.exists());
	}
	
	@Test
	public void testGetFileByMethod() {
		File testFile = fcs.getFileByMethod(method);
		assertEquals("string.json",testFile.getName());
		assertEquals("mscorlib", testFile.getParentFile().getName());
		File secondTestFile = fcs.getFileByMethod(method);
		assertEquals("string.json", secondTestFile.getName());
		assertEquals("mscorlib", secondTestFile.getParentFile().getName());
	}
	
	@Test
	public void testInitFile() {
		File newFile = new File("src/test/java/parser/NewFile.json");
		fcs.initFile(newFile, "DeclaringTypeName");
		assertTrue(newFile.exists());
		assertTrue(newFile.isFile());
		File folder = new File("src/test/java/parser");
		assertEquals(newFile.getParentFile(), folder);
	}
	
	@After
	public void tearDown() {
		new File("src/test/java/parser/NewFile.json").delete();
		new File("src/test/java/parser/Output.json").delete();
		new File("src/test/java/parser/testarchive/mscorlib/string.json").delete();
		new File("src/test/java/parser/testarchive/mscorlib").delete();
		new File("src/test/java/parser/testarchive").delete();
	}

}
