package parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import parser.model.Method;
import parser.model.MethodCollection;

public class FileService implements IFileService {

	private File directory;

	public FileService(String archiveName) {
		directory = new File(archiveName);
		directory.mkdir();
	}

	public void addMethod(IMethodName method) {
		File file = getFileByMethod(method);
		MethodCollection collection = getCollection(file);
		if (collection == null) {
			System.out.println("Error getting method-collection");
			return;
		}
		List<Method> methodList = collection.getMethods();
		Iterator<Method> iterator = methodList.iterator();
		String methodName = getMethodName(method);
		while (iterator.hasNext()) {
			Method currentMethod = iterator.next();
			if (currentMethod.getName().equals(methodName)) {
				// method already exists in file: increase its counter and replace list-entry
				int index = methodList.indexOf(currentMethod);
				currentMethod.setCount(currentMethod.getCount() + 1);
				methodList.set(index, currentMethod);
				collection.setMethods(methodList);
				writeCollectionToFile(collection, file);
				return;
			}
		}
		// method does not exist in file: generate a new method-entry
		Method newMethod = new Method(methodName, method.isConstructor(), 1);
		methodList.add(newMethod);
		collection.setMethods(methodList);
		writeCollectionToFile(collection, file);
	}

	private String getMethodName(IMethodName method) {
		String simpleName = method.getDeclaringType().getFullName();
		if(!method.isConstructor()) {
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

	private MethodCollection getCollection(File file) {
		MethodCollection collection = null;
		try {
			FileReader reader = new FileReader(file);
			collection = new Gson().fromJson(reader, MethodCollection.class);
			return collection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collection;
	}

	private void writeCollectionToFile(MethodCollection collection, File file) {
		try {
			FileWriter writer = new FileWriter(file.getAbsolutePath());
			writer.write(new Gson().toJson(collection));
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private File getFileByMethod(IMethodName method) {
		File file = new File(this.directory, method.getDeclaringType().getFullName() + ".json");
		if (!file.exists()) {
			initFile(file);
		}
		return file;
	}

	private void initFile(File file) {
		// FIXME: handle too long filenames
		Method[] method = {};
		MethodCollection collection = new MethodCollection(file.getName().split("\\.json")[0], Arrays.asList(method));
		try {
			FileWriter writer = new FileWriter(file.getAbsolutePath());
			writer.write(new Gson().toJson(collection));
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
