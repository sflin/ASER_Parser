package parser;

import java.io.File;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import parser.model.ClassCollection;
import parser.model.MethodCollection;

public interface IFileCreatorService {

	void addMethod(IMethodName method);
	
	String getMethodName(IMethodName method);
	
	MethodCollection getMethodCollectionByClass(String methodCollectionName, ClassCollection classCollection);
	
	void writeCollectionToFile(ClassCollection collection, File file);
	
	File getFileByMethod(IMethodName method);
	
	void initFile(File file, String declaringTypeName);

}
