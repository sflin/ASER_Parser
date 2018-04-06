package parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.io.Directory;

public class FileService implements IFileService {

	private static Directory directory;

	public FileService(String archiveName) {
		setDirectory(new Directory(archiveName));
		directory.createDirectory(archiveName);
	}

	public void addMethod(IMethodName method) {
		File file = getFileByMethod(method); // TODO: only fileName?
		Type collectionType = new TypeToken<MethodCollection>() {
			private static final long serialVersionUID = 1L;
		}.getType();
//		Type mapType = new TypeToken<Map<String, Object>>() {
//			private static final long serialVersionUID = 1L;
//		}.getType();
		try {
			FileReader reader = new FileReader(file);
			MethodCollection collection = new Gson().fromJson(reader, collectionType);
			List<Method> methodList = collection.getMethods();
			Iterator<Method> iterator = methodList.iterator();
//			Map<String, Object> methodList = new Gson().fromJson(map, mapType);
//			ArrayList<Map<String, Object>> methodList = (ArrayList<Map<String, Object>>) map.get("methods");
			String methodName = method.isConstructor() ? method.getFullName() : method.getFullName(); // TODO
//			for(HashMap<String, Object> obj : methodList) {
//				System.out.println(obj.get("name"));
//			}
//			Iterator<HashMap<String, Object>> iterator = methodList.iterator();
			while (iterator.hasNext()) {
				Method m = iterator.next();
				if(m.getName().equals(methodName)) {
					System.out.println("True");
//				HashMap<String, Object> methodmap = iterator.next();
//				if (methodmap.get("name").equals(methodName)) {
					// method already exists in file, so increase its counter and replace
					// corresponding list-entry
					int index = methodList.indexOf(m);
					Integer counter = Integer.parseInt(m.getCount().toString()) + 1;
					m.setCount(counter);
					methodList.set(index, m);
					collection.setMethods(methodList);
//					new Gson().toJson(collection, new FileWriter(file));
					try {
						// write converted json data to a file named "CountryGSON.json"
						FileWriter writer = new FileWriter(file.getName());
						writer.write(new Gson().toJson(collection));
						writer.close();

					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
//			// method does not exist in file: generate a new method-entry
			Method newMethod = new Method(methodName, method.isConstructor(), new Integer(1));
//			System.out.println(newMethod.getName() + " " + newMethod.isConstructor()  + " " +  newMethod.getCount());
			methodList.add(newMethod);
			collection.setMethods(methodList);
//			new Gson().toJson(collection, new FileWriter(file));
			try {
				// write converted json data to a file named "CountryGSON.json"
				FileWriter writer = new FileWriter(file.getName());
				writer.write(new Gson().toJson(collection));
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
//			HashMap<String, Object> newMethod = new HashMap<String, Object>();
//			newMethod.put("method", methodName);
//			newMethod.put("isConstructor", method.isConstructor());
//			newMethod.put("count", 1);
//			methodList.add(newMethod);
//			map.remove("methods");
//			map.put("methods", methodList);
//			new Gson().toJson(map, new FileWriter(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File getFileByMethod(IMethodName method) {
		Directory dir = getDirectory();
		if(dir.exists(method.getFullName())) { // check whether file exists, if so, return it
			System.out.println("File exists");
			return new File(method.getFullName() + ".json");
		}
		else {
			File file = new File(method.getFullName() + ".json");
//			try {
//				file.createNewFile();
				initFile(file, method.isConstructor());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return file;
		}
	}

	public static Directory getDirectory() {
		return directory;
	}

	public static void setDirectory(Directory directory) {
		FileService.directory = directory;
	}
	
	private void initFile(File file, boolean isConstructor) {

//		Method method = new Method("", isConstructor, new Integer(0));
		Method[] method = {};
		MethodCollection col = new MethodCollection(file.getName(), Arrays.asList(method));
		// String method = "{'name': 'aaa','isConstructor': true,'count': 12}";
		try {
			// write converted json data to a file named "CountryGSON.json"
			FileWriter writer = new FileWriter(file.getName());
			writer.write(new Gson().toJson(col));
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// String jsonInString = "{'fullname' : '" + file.getName() +
		// "',\n'methods':\n[" + method + "]}";
		// Writer writer = new FileWriter(file.getName());
		// Gson gson = new GsonBuilder().create();
		// gson.toJson(jsonInString, writer);
		// // new Gson().toJson(jsonInString, new FileWriter(file));
		// }

		// catch (JsonIOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

}
