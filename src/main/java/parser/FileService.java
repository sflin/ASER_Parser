package parser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.io.ReadingArchive;

public class FileService implements IFileService {

	private static ReadingArchive archive;

	public FileService(String archiveName) {
		File file = new File(archiveName);
		archive = new ReadingArchive(file);
	}

	public void addMethod(IMethodName method) {
		File file = getFileByMethod(method); // TODO: only fileName?
		Type mapType = new TypeToken<HashMap<String, Object>>() {
			private static final long serialVersionUID = 1L;
		}.getType();
		try {
			HashMap<String, Object> map = new Gson().fromJson(new FileReader(file), mapType);
			ArrayList<HashMap<String, Object>> methodList = (ArrayList<HashMap<String, Object>>) map.get("methods");
			String methodName = method.isConstructor() ? method.getFullName() : method.getFullName(); // TODO
			Iterator<HashMap<String, Object>> iterator = methodList.iterator();
			while (iterator.hasNext()) {
				HashMap<String, Object> methodmap = iterator.next();
				if (methodmap.get("name").equals(methodName)) {
					// method already exists in file, so increase its counter and replace
					// corresponding list-entry
					int index = methodList.indexOf(methodmap);
					Integer counter = Integer.parseInt(methodmap.get("count").toString()) + 1;
					methodmap.remove("count");
					methodmap.put("count", counter);
					methodList.set(index, methodmap);
					map.remove("methods");
					map.put("methods", methodList);
					new Gson().toJson(map, new FileWriter(file));
					return;
				}
			}
			// method does not exist in file: generate a new method-entry
			HashMap<String, Object> newMethod = new HashMap<String, Object>();
			newMethod.put("method", methodName);
			newMethod.put("isConstructor", method.isConstructor());
			newMethod.put("count", 1);
			methodList.add(newMethod);
			map.remove("methods");
			map.put("methods", methodList);
			new Gson().toJson(map, new FileWriter(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private File getFileByMethod(IMethodName method) {
		while (archive.hasNext()) {
			File file = archive.getNext(File.class);
			if (file.getName().equals(method.getFullName())) {
				archive.close();
				return file; // file already exists, so return it
			}
		}
		archive.close();
		return new File(method.getFullName() + ".json");
	}

}
