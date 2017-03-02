/**
 * 
 */
package com.ipmetric.p1.server;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertyInfo {
	final static Logger logger = Logging.getLogger();

	static private String[] propertyFiles = new String[] { "resources/conf/WSCR.properties", "conf/WSCR.properties",
			"WSCR.properties", "/resources/conf/WSCR.properties", "/conf/WSCR.properties", "/WSCR.properties",
			"/tmp/WSCR.properties" };

	static private String findFile(final String[] from) {
		for (final String fn : from) {
			final File file = new File(fn);
			if (file.canRead()) {
				System.out.println("fn=" + fn);
				return fn;
			}
		}
		return ".property";
	}

	final String propertyFilename = findFile(propertyFiles);
	final File propertyFile = new File(propertyFilename);
	Long timeLastRead = Long.MIN_VALUE;
	Properties properties = new Properties();

	public PropertyInfo() {
		if (isStale()) {
			load();
		}
	}

	/***************************************************************************
	 * Load the property file, updating time stamp and contents only if the read is successful.
	 */
	void load() {
		try {
			final FileInputStream fis = new FileInputStream(propertyFile);
			final Properties newProperties = new Properties();
			newProperties.load(fis);
			timeLastRead = propertyFile.lastModified();
			properties = newProperties;
			fis.close();
		} catch (final Exception e) {
			logger.info("Property file " + propertyFile + " " + e.getMessage());
		}
	}

	boolean isStale() {
		return propertyFile.lastModified() > timeLastRead;
	}

	// Request for one single property presumably does not need the most current
	public String getProperty(final String propertyName) {
		return properties.getProperty(propertyName);
	}

	public String getProperty(final String propertyName, final String defaultValue) {
		return properties.getProperty(propertyName, defaultValue);
	}

	public String getFreshPropertyvalue(final String propertyName) {
		if (isStale()) {
			logger.info("getProperties: is stale (reloading");
			load();
		}
		return getProperty(propertyName);
	}

	// Request for the property list presumably needs the most current, and will reuse it.
	public Properties getProperties() {
		if (isStale()) {
			logger.info("getProperties: is stale (reloading");
			load();
		}
		return properties;
	}

	/***************************************************************************
	 * Returns an array of elements having a common property prefix, for the current property set
	 * 
	 * @param numAttr
	 *            The number of items
	 * @param prefix
	 *            The prefix, which typically should end with a "." as the delimiter.
	 * @return An array containing the values found, or null if a value was not found.
	 */
	String[] getpvec(final String numAttr, final String prefix) {
		String[] result;
		if (numAttr != null) {
			final int attrCount = Integer.parseInt(numAttr);
			result = new String[attrCount];
			for (int i = 0; i < attrCount; i++) {
				result[i] = this.properties.getProperty(prefix + i);
			}
		} else {
			result = new String[0];
		}
		return result;
	}
}