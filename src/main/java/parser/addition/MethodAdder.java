package parser.addition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;

import cc.kave.commons.model.naming.types.ITypeName;
import parser.model.ClassCollection;
import parser.model.Method;
import parser.model.MethodCollection;

public abstract class MethodAdder implements IMethodAdder {
	
	private static Logger LOG = Logger.getLogger(MethodAdder.class.getName());
	private File directory;

	public MethodAdder(String archiveName) {
		directory = new File(archiveName);
		directory.mkdir();
	}

	public abstract void addMethod(Object object);
	
	public abstract String getName(Object object);

	@Override
	public MethodCollection getMethodCollectionByClass(String methodCollectionName, ClassCollection classCollection) {
		List<MethodCollection> collectionList = classCollection.getCollections();
		for (Iterator<MethodCollection> iterator = collectionList.iterator(); iterator.hasNext();) {
			MethodCollection currentMethodCollection = iterator.next();
			if (currentMethodCollection.getFullName().equals(methodCollectionName)) {
				return currentMethodCollection; // of form {"fullname": "ABC","methods": [] }
			}
		}
		return null;
	}

	@Override
	public ClassCollection getClassCollection(File file) {
		ClassCollection collection = null;
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
			collection = new Gson().fromJson(reader, ClassCollection.class);
		} catch(IOException e) {
			LOG.warning("Error getting classCollection for file " + file.getName() + "! Caught an " + e.getClass().getName());
		}
		return collection;
	}

	@Override
	public void writeCollectionToFile(ClassCollection collection, File file) {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(file),
				Charset.forName("UTF-8").newEncoder())) {
			writer.write(new Gson().toJson(collection));

		} catch (IOException e) {
			LOG.warning("Error writing collection to file " + file.getName() + "! Caught an " + e.getClass().getName());
		}
	}

	@Override
	public File getFileByDeclaringType(ITypeName type) {
		String assembly = type.getAssembly().getName();
		File assemblyFolder = new File(this.directory, assembly);
		if (!assemblyFolder.exists()) {
			assemblyFolder.mkdir();
		}
		File file = new File(assemblyFolder, type.getName() + ".json");
		if (!file.exists()) {
			initFile(file, type.getFullName());
		}
		return file;
	}

	@Override
	public void initFile(File file, String declaringTypeName) {
		Method[] method = {};
		MethodCollection methodCollection = new MethodCollection(declaringTypeName, Arrays.asList(method));
		ClassCollection classCollection = new ClassCollection(Arrays.asList(methodCollection));
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8").newEncoder())) {
			writer.write(new Gson().toJson(classCollection));
		} catch (IOException e) {
			LOG.warning("Error init file " + file.getName() + "! Caught an " + e.getClass().getName());
		}
	}
}
