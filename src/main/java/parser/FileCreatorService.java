package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import parser.model.ClassCollection;
import parser.model.Method;
import parser.model.MethodCollection;

public class FileCreatorService implements IFileCreatorService {
	
	private static Logger LOG = Logger.getLogger(FileCreatorService.class.getName());

	private File directory;
	private static boolean methodExists;

	public FileCreatorService(String archiveName) {
		directory = new File(archiveName);
		directory.mkdir();
		methodExists = false;
	}

	public void addMethod(IMethodName method) {
		File file = getFileByDeclaringType(method.getDeclaringType());
		ClassCollection classCollection = getClassCollection(file);
		if (classCollection == null) {
			LOG.warning("Could not resolve classCollection for " + method.getFullName());
			return;
		}
		MethodCollection collection = getMethodCollectionByClass(method.getDeclaringType().getFullName(), classCollection);
		if (collection == null) {
			// collection not yet existing
			Method[] tmp = {};
			collection = new MethodCollection(method.getDeclaringType().getFullName(), new ArrayList<>(Arrays.asList(tmp)));
			classCollection.getCollections().add(collection);
		}
		int mcIndex = classCollection.getCollections().indexOf(collection);
		List<Method> methodList = collection.getMethods();
		String methodName = getMethodName(method);
		for (Iterator<Method> iterator = methodList.iterator(); iterator.hasNext() && !methodExists;) {
			Method currentMethod = iterator.next();
			if (currentMethod.getName().equals(methodName)) {
				// method already exists in file: increase its counter and replace list-entry
				int index = methodList.indexOf(currentMethod);
				currentMethod.setCount(currentMethod.getCount() + 1);
				methodList.set(index, currentMethod);
				methodExists = true;
			}
		}
		if (!methodExists) { // generate new method-entry
			Method newMethod = new Method(methodName, method.isConstructor(), false, 1);
			methodList.add(newMethod);
		}
		collection.setMethods(methodList);
		List<MethodCollection> methodCollectionList = classCollection.getCollections();
		methodCollectionList.set(mcIndex, collection);
		classCollection.setCollections(methodCollectionList);
		writeCollectionToFile(classCollection, file);
		methodExists = false;
	}

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

	public String getMethodName(IMethodName method) {
		String simpleName = method.getDeclaringType().getFullName();
		if (!method.isConstructor()) {
			simpleName += "." + method.getName();
		}
		simpleName += "(";
		Iterator<IParameterName> iter = method.getParameters().iterator();
		while (iter.hasNext()) {
			IParameterName param = iter.next();
			simpleName += param.getValueType().getFullName() + " " + param.getName();
			if (iter.hasNext()) {
				simpleName += ", ";
			}
		}
		simpleName += ")";
		return simpleName;
	}

	public ClassCollection getClassCollection(File file) {
		ClassCollection collection = null;
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
			collection = new Gson().fromJson(reader, ClassCollection.class);
		} catch(IOException e) {
			LOG.warning("Error getting classCollection for file " + file.getName() + "! Caught an " + e.getClass().getName());
		}
		return collection;
	}

	public void writeCollectionToFile(ClassCollection collection, File file) {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(file),
				Charset.forName("UTF-8").newEncoder())) {
			writer.write(new Gson().toJson(collection));

		} catch (IOException e) {
			LOG.warning("Error writing collection to file " + file.getName() + "! Caught an " + e.getClass().getName());
		}
	}

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

	public void addCastMethod(ITypeName typeName) {
		File file = getFileByDeclaringType(typeName);
		ClassCollection classCollection = getClassCollection(file);
		if (classCollection == null) {
			LOG.warning("Could not resolve classCollection for CastExpression of " + typeName.getName());
			return;
		}
		MethodCollection collection = getMethodCollectionByClass(typeName.getFullName(), classCollection);
		if (collection == null) {
			// collection not yet existing
			Method[] tmp = {};
			collection = new MethodCollection(typeName.getFullName(), new ArrayList<>(Arrays.asList(tmp)));
			classCollection.getCollections().add(collection);
		}
		int mcIndex = classCollection.getCollections().indexOf(collection);
		List<Method> methodList = collection.getMethods();
		String methodName = getCastName(typeName);
		for (Iterator<Method> iterator = methodList.iterator(); iterator.hasNext() && !methodExists;) {
			Method currentMethod = iterator.next();
			if (currentMethod.getName().equals(methodName)) {
				// method already exists in file: increase its counter and replace list-entry
				int index = methodList.indexOf(currentMethod);
				currentMethod.setCount(currentMethod.getCount() + 1);
				methodList.set(index, currentMethod);
				methodExists = true;
			}
		}
		if (!methodExists) { // generate new method-entry
			Method newMethod = new Method(methodName, false, true, 1);
			methodList.add(newMethod);
		}
		collection.setMethods(methodList);
		List<MethodCollection> methodCollectionList = classCollection.getCollections();
		methodCollectionList.set(mcIndex, collection);
		classCollection.setCollections(methodCollectionList);
		writeCollectionToFile(classCollection, file);
		methodExists = false;
	}

	public String getCastName(ITypeName typeName) {
		String name = "(" + typeName.getFullName() + ") object";
		return name;
	}

}
