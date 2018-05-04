package parser.addition;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import parser.model.ClassCollection;
import parser.model.Method;
import parser.model.MethodCollection;

public class SimpleMethodAdder extends MethodAdder {
	
	private static Logger LOG = Logger.getLogger(SimpleMethodAdder.class.getName());
	private static boolean methodExists;

	public SimpleMethodAdder(String archiveName) {
		super(archiveName);
		methodExists = false;
	}
	
	@Override
	public void addMethod(Object object) {
		IMethodName method = (IMethodName) object;
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
		String methodName = getName(method);
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
			String type = method.isConstructor() ? "CTOR" : "SIMPLE";
			Method newMethod = new Method(methodName, type, 1);
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
		IMethodName method = (IMethodName) object; 
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
}
