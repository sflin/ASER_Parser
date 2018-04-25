package examples;

import java.io.File;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.ReadingArchive;
import parser.CastVisitor;
import parser.InvocationVisitor;

public class Parser {

	public static void main(String[] args) {
		run(args[0]);
	}

	public static void run(String contextArchive) {
		File f = new File(contextArchive);
		try (ReadingArchive ra = new ReadingArchive(f)){
			int counter = 0;
			while (ra.hasNext() && counter < 15) {
				Context ctx = ra.getNext(Context.class);
				counter += 1;
				ctx.getSST().accept(new CastVisitor(), null);
				ctx.getSST().accept(new InvocationVisitor(), null);
			}
		}
	}

}
