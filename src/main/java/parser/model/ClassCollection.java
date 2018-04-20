package parser.model;

import java.util.List;

public class ClassCollection {
	
	private List<MethodCollection> collections;
	
	public ClassCollection(List<MethodCollection> collections) {
		this.collections = collections;
	}

	public List<MethodCollection> getCollections() {
		return collections;
	}

	public void setCollections(List<MethodCollection> collections) {
		this.collections = collections;
	}

}
