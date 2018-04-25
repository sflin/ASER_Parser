package parser.model;

public class Method {
	
	private String name;
	private boolean isConstructor;
	private boolean isCast;
	private Integer count;
	
	public Method(String name, boolean isConstructor, boolean isCast, int count) {
		this.name = name;
		this.isConstructor = isConstructor;
		this.isCast = isCast;
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
	public boolean isCast() {
		return isCast;
	}
	public void setCast(boolean isCast) {
		this.isCast = isCast;
	}
}
