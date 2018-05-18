package parser.addition;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import cc.kave.commons.model.naming.types.ITypeName;
import parser.model.ClassCollection;
import parser.model.Method;
import parser.model.MethodCollection;

public class CastAdder extends MethodAdder {
	
	private static Logger LOG = Logger.getLogger(CastAdder.class.getName());
	
	private static boolean methodExists;

	public CastAdder(String archiveName) {
		super(archiveName);
		methodExists = false;
	}

	@Override
	public void addMethod(Object object) {
		ITypeName typeName = (ITypeName) object;
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
		String methodName = getName(typeName);
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
			Method newMethod = new Method(methodName, "CAST", 1);
			methodList.add(newMethod);
		}
		collection.setMethods(methodList);
		List<MethodCollection> methodCollectionList = classCollection.getCollections();
		methodCollectionList.set(mcIndex, collection);
		classCollection.setCollections(methodCollectionList);
		writeCollectionToFile(classCollection, file);
		methodExists = false;
	}
	
	@Override
	public String getName(Object object) {
		ITypeName typeName = (ITypeName) object;
		String name = "(" + typeName.getFullName() + ") object";
		return name;
	}
}
