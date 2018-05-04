package parser;

import java.io.File;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.ReadingArchive;
import parser.visitor.ContextVisitor;

public class Parser {
	
	private static final String DIR_ARCHIVE = System.getProperty("user.home") + File.separator + "archive";

	public static void main(String[] args) {
		run(args[0]);
	}

	public static void run(String contextArchive) {
		File f = new File(contextArchive);
		try (ReadingArchive ra = new ReadingArchive(f)){
//			int counter = 0;
			while (ra.hasNext()) {// && counter < 5){ 
				Context ctx = ra.getNext(Context.class);
//				counter +=1;
				ctx.getSST().accept(new ContextVisitor(DIR_ARCHIVE), null);
			}
		}
	}

}
