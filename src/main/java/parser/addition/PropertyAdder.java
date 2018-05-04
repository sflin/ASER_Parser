package parser.addition;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import cc.kave.commons.model.naming.codeelements.IPropertyName;
import parser.model.ClassCollection;
import parser.model.Method;
import parser.model.MethodCollection;

public class PropertyAdder extends MethodAdder {
	
	private static Logger LOG = Logger.getLogger(MethodAdder.class.getName());
	private static boolean methodExists;

	public PropertyAdder(String archiveName) {
		super(archiveName);
		methodExists = false;
	}

	@Override
	public void addMethod(Object object) {
		IPropertyName propertyName = (IPropertyName) object;
		File file = getFileByDeclaringType(propertyName.getDeclaringType());
		ClassCollection classCollection = getClassCollection(file);
		if (classCollection == null) {
			LOG.warning("Could not resolve classCollection for " + propertyName.getFullName());
			return;
		}
		MethodCollection collection = getMethodCollectionByClass(propertyName.getDeclaringType().getFullName(), classCollection);
		if (collection == null) {
			// collection not yet existing
			Method[] tmp = {};
			collection = new MethodCollection(propertyName.getDeclaringType().getFullName(), new ArrayList<>(Arrays.asList(tmp)));
			classCollection.getCollections().add(collection);
		}
		int mcIndex = classCollection.getCollections().indexOf(collection);
		List<Method> methodList = collection.getMethods();
		String methodName = getName(propertyName);
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
			Method newMethod = new Method(methodName, "PROP", 1);
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
		IPropertyName propertyName = (IPropertyName) object;
		return propertyName.getDeclaringType().getFullName() + "." + propertyName.getFullName();
	}

}
