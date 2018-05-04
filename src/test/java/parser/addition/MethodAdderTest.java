package parser.addition;

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
import cc.kave.commons.model.naming.impl.v0.codeelements.PropertyName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import parser.model.ClassCollection;
import parser.model.MethodCollection;

public class MethodAdderTest {
	
	private MethodName method;
	private MethodName constructor;
	private TypeName typeName;
	private PropertyName propertyName;
	private ClassCollection classCollection;
	private MethodAdder simpleAdder;
	private MethodAdder castAdder;
	private MethodAdder propertyAdder;
	
	@Before
	public void setup() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		simpleAdder = new SimpleMethodAdder("src/test/java/parser/testarchive");
		castAdder = new CastAdder("src/test/java/parser/testarchive");
		propertyAdder = new PropertyAdder("src/test/java/parser/testarchive");
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(new File("src/test/java/parser/addition/TestFile.json")), "UTF-8")) {
			classCollection = new Gson().fromJson(reader, ClassCollection.class);
		}
		method = new MethodName("0M:static [p:string] [p:string].Format([p:string] format, [p:object] arg0, [p:object] arg1)");
		constructor = new MethodName("0M:[p:void] [System.Runtime.Serialization.Formatters.Binary.BinaryFormatter, mscorlib, 4.0.0.0]..ctor()");
		typeName = new TypeName("0T:System.Drawing.Bitmap, System.Drawing, 4.0.0.0"); 
		propertyName = new PropertyName("0P:get [p:int] [System.Array, mscorlib, 4.0.0.0].Length()");
		
	}

	@Test
	public void testAddSimpleMethod() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		simpleAdder.addMethod(method);
		File testFile = new File("src/test/java/parser/testarchive/mscorlib/string.json");
		assertTrue(testFile.exists());
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			String actualName = testCollection.getCollections().get(0).getMethods().get(0).getName();
			String expectedName = "System.String.Format(System.String format, System.Object arg0, System.Object arg1)";
			assertEquals(expectedName, actualName);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(1), actualCount);
			String type = testCollection.getCollections().get(0).getMethods().get(0).getType();
			assertEquals("SIMPLE", type);
		}
		simpleAdder.addMethod(method);
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(2), actualCount);
		}
	}

	@Test
	public void testAddConstructor() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		simpleAdder.addMethod(constructor);
		File testFile = new File("src/test/java/parser/testarchive/mscorlib/BinaryFormatter.json");
		assertTrue(testFile.exists());
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			String actualName = testCollection.getCollections().get(0).getMethods().get(0).getName();
			String expectedName = "System.Runtime.Serialization.Formatters.Binary.BinaryFormatter()";
			assertEquals(expectedName, actualName);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(1), actualCount);
			String type = testCollection.getCollections().get(0).getMethods().get(0).getType();
			assertEquals(type, "CTOR");
		}
	}
	
	@Test
	public void testAddCastMethod() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		castAdder.addMethod(typeName);
		File testFile = new File("src/test/java/parser/testarchive/System.Drawing/Bitmap.json");
		assertTrue(testFile.exists());
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			String actualName = testCollection.getCollections().get(0).getMethods().get(0).getName();
			String expectedName = "(0T:System.Drawing.Bitmap) object";
			assertEquals(expectedName, actualName);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(1), actualCount);
			String type = testCollection.getCollections().get(0).getMethods().get(0).getType();
			assertEquals(type, "CAST");
		}
		castAdder.addMethod(typeName);
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(2), actualCount);
		}
	}
	
	@Test
	public void testAddPropertyMethod() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		propertyAdder.addMethod(propertyName);
		File testFile = new File("src/test/java/parser/testarchive/mscorlib/Array.json");
		assertTrue(testFile.exists());
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			String actualName = testCollection.getCollections().get(0).getMethods().get(0).getName();
			String expectedName = "System.Array.Length";
			assertEquals(expectedName, actualName);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(1), actualCount);
			String type = testCollection.getCollections().get(0).getMethods().get(0).getType();
			assertEquals(type, "PROP");
		}
		propertyAdder.addMethod(propertyName);
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(testFile), "UTF-8")) {
			ClassCollection testCollection = new Gson().fromJson(reader, ClassCollection.class);
			Integer actualCount = testCollection.getCollections().get(0).getMethods().get(0).getCount();
			assertEquals(new Integer(2), actualCount);
		}
	}
	
	@Test
	public void testGetMethodName() {
		String methodName = simpleAdder.getName(method);
		String expectedName = "System.String.Format(System.String format, System.Object arg0, System.Object arg1)";
		assertEquals(expectedName, methodName);
		methodName = simpleAdder.getName(constructor);
		expectedName = "System.Runtime.Serialization.Formatters.Binary.BinaryFormatter()";
		assertEquals(expectedName, methodName);
	}
	
	@Test
	public void testGetClassCollection() { 
		ClassCollection testClassCollection = simpleAdder.getClassCollection(new File("src/test/java/parser/addition/TestFile.json"));
		assertNotNull(testClassCollection);
		assertEquals(testClassCollection.getClass(), ClassCollection.class);
	}

	@Test
	public void testGetMethodCollectionByClass() {
		MethodCollection testCollection = simpleAdder.getMethodCollectionByClass("System.String", classCollection);
		assertEquals(testCollection, classCollection.getCollections().get(0));
		MethodCollection nullCollection = simpleAdder.getMethodCollectionByClass("notExistingMethod", classCollection);
		assertNull(nullCollection);
	}
	
	@Test
	public void testWriteCollectionToFile() {
		ClassCollection collection = new ClassCollection(null);
		File f = new File("src/test/java/parser/Output.json");
		simpleAdder.writeCollectionToFile(collection, f);
		assertTrue(f.exists());
	}
	
	@Test
	public void testGetFileByMethod() {
		File testFile = simpleAdder.getFileByDeclaringType(method.getDeclaringType());
		assertEquals("string.json",testFile.getName());
		assertEquals("mscorlib", testFile.getParentFile().getName());
		File secondTestFile = simpleAdder.getFileByDeclaringType(method.getDeclaringType());
		assertEquals("string.json", secondTestFile.getName());
		assertEquals("mscorlib", secondTestFile.getParentFile().getName());
	}
	
	@Test
	public void testInitFile() {
		File newFile = new File("src/test/java/parser/NewFile.json");
		simpleAdder.initFile(newFile, "DeclaringTypeName");
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
		new File("src/test/java/parser/testarchive/mscorlib/Array.json").delete();
		new File("src/test/java/parser/testarchive/mscorlib/BinaryFormatter.json").delete();
		new File("src/test/java/parser/testarchive/System.Drawing/Bitmap.json").delete();
		new File("src/test/java/parser/testarchive/mscorlib").delete();
		new File("src/test/java/parser/testarchive/System.Drawing").delete();
		new File("src/test/java/parser/testarchive").delete();
	}

}
