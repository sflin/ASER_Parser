import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.ReadingArchive;

public class KaveClient {
	
	public static void main (String [] args) {
		run();
	}
	public static void run() {
		File f = new File("/home/selin/Documents/Contexts-170503/Antaris/RazorEngine/src/RazorEngine.sln-contexts.zip");
		ReadingArchive ra = new ReadingArchive(f);
		List<String> names = new ArrayList<String>();
		
//		try (ra = new ReadingArchive(f)) {
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				InvocationVisitorContext context = new InvocationVisitorContext(ctx);
				ctx.getSST().accept(new InvocationVisitor(), context);
				for(String m : context.getMethods()) {
					if(!names.contains(m)) {
						names.add(m);
					}
				}
				}
			
			
			System.out.println(names);
			System.out.println(names.size());
//		}
	}

}
