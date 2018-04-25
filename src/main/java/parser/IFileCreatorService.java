package parser;

import java.io.File;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import parser.model.ClassCollection;
import parser.model.MethodCollection;

public interface IFileCreatorService {

	void addMethod(IMethodName method);
	
	void addCastMethod(ITypeName typeName);
	
	String getMethodName(IMethodName method);
	
	String getCastName(ITypeName typeName);
	
	ClassCollection getClassCollection(File file);
	
	MethodCollection getMethodCollectionByClass(String methodCollectionName, ClassCollection classCollection);
	
	void writeCollectionToFile(ClassCollection collection, File file);
	
	File getFileByDeclaringType(ITypeName type);
	
	void initFile(File file, String declaringTypeName);

}
