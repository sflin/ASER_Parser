package parser.model;

public class Method {
	
	private String name;
	private String type;
	private Integer count;
	
	public Method(String name, String type, int count) {
		this.name = name;
		this.type = type;
		this.count = count;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
