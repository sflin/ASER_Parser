package parser;

public class Method {
	
	private String name;
	private boolean isConstructor;
	private Integer count;
	
	public Method(String name, boolean isConstructor, int count) {
		this.name = name;
		this.isConstructor = isConstructor;
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isConstructor() {
		return isConstructor;
	}
	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
