package examples;

import java.io.File;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.ReadingArchive;
import parser.InvocationVisitor;

public class Parser {

	private static final String DIR_CONTEXTS = "C:\\temp\\Contexts";
	private static final String DIR_ARCHIVE = "C:\\temp\\MethodCollections";
	public static void main(String[] args) {
		run();
	}

	public static void run() {
		System.out.println("Start Process");
		for (String zip : IoHelper.findAllZips(DIR_CONTEXTS)) {
			File f = new File(zip);
			try (ReadingArchive ra = new ReadingArchive(f)){
				while (ra.hasNext()) {
					Context ctx = ra.getNext(Context.class);
					System.out.println("Create MethodCollection for "+ctx.getSST().getEnclosingType().getName());
					ctx.getSST().accept(new InvocationVisitor(DIR_ARCHIVE), null);
				}
			}
		}
	}

}
