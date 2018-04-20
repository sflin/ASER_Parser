package parser.model;

import java.util.List;

public class MethodCollection {
	
	private String fullName;
	private List<Method> methods;
	
	public MethodCollection(String fullName, List<Method> methods) {
		this.fullName = fullName;
		this.methods = methods;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

}
