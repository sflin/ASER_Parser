package parser;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.ReadingArchive;
import parser.visitor.ContextVisitor;

public class Parser {

	private static Logger LOG = Logger.getLogger(Parser.class.getName());

	private static String CONTEXT_DATA = System.getProperty("user.home") + File.separator + "contexts";
	private static String DIR_ARCHIVE = System.getProperty("user.home") + File.separator + "archive";

	public static void main(String[] args) {
		if (args.length == 1) {
			LOG.severe("ERROR: Invalid number of arguments!");
			return;
		} else if (args.length == 2) {
			CONTEXT_DATA = args[0];
			DIR_ARCHIVE = args[1];
		}
		run(CONTEXT_DATA, DIR_ARCHIVE);
	}

	public static void run(String contextData, String contextArchive) {
		for (String context : findAllContexts()) {
			File f = new File(context);
			try (ReadingArchive ra = new ReadingArchive(f)) {
				while (ra.hasNext()) {
					Context ctx = ra.getNext(Context.class);
					ctx.getSST().accept(new ContextVisitor(DIR_ARCHIVE), null);
				}
			}
		}
	}

	private static List<String> findAllContexts() {
		List<String> zips = Lists.newLinkedList();
		for (File f : FileUtils.listFiles(new File(CONTEXT_DATA), new String[] { "zip" }, true)) {
			zips.add(f.getAbsolutePath());
		}
		return zips;
	}

}
