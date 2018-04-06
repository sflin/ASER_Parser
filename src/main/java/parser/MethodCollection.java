package parser;

import java.util.List;

public class MethodCollection {
	
	private String fullname;
	private List<Method> methods;
	
	public MethodCollection(String fullname, List<Method> methods) {
		this.fullname = fullname;
		this.methods = methods;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

}
