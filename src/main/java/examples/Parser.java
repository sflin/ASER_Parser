package examples;

import java.io.File;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.ReadingArchive;
import parser.InvocationVisitor;

public class Parser {

	public static void main(String[] args) {
		run();
	}

	public static void run() {
		File f = new File("/home/selin/Documents/Contexts-170503/Antaris/RazorEngine/src/RazorEngine.sln-contexts.zip");
		try (ReadingArchive ra = new ReadingArchive(f)){
			int counter = 0;
			while (ra.hasNext()  && counter < 5) {
				counter += 1;
				Context ctx = ra.getNext(Context.class);
				ctx.getSST().accept(new InvocationVisitor(), null);
			}
		}
	}

}
