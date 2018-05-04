package parser.addition;

import java.io.File;

import cc.kave.commons.model.naming.types.ITypeName;
import parser.model.ClassCollection;
import parser.model.MethodCollection;

public interface IMethodAdder {

	void addMethod(Object object);
	
	String getName(Object object);
	
	ClassCollection getClassCollection(File file);
	
	MethodCollection getMethodCollectionByClass(String methodCollectionName, ClassCollection classCollection);
	
	void writeCollectionToFile(ClassCollection collection, File file);
	
	File getFileByDeclaringType(ITypeName type);
	
	void initFile(File file, String declaringTypeName);

}
