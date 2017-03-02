package com.ipmetric.p1.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

public class Logging {
	static private Logger log;
	//static private String logfile = "log4j.properties";

	static {
		log = Logger.getLogger("Logging");
		//PropertyConfigurator.configureAndWatch(logfile);
	}

	public static Logger getLogger() {
		return log;
	}

	public static void exception(final Exception e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		log.warning(e.getMessage());
		log.warning(sw.getBuffer().toString());
	}

}