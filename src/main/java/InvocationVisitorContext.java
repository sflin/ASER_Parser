
import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;

public class InvocationVisitorContext {
	
	List<String> methods = new ArrayList<String>();
	public InvocationVisitorContext(Context context) {
		
	}
	
	public void addMethod(String method) {
		if(!methods.contains(method)) {
			methods.add(method);
		}
	}
	
	public List<String> getMethods(){
		return methods;
	}

}
