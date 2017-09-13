/*
 * Copyright (C), 2011-2012, Sunrise Tech. Co., Ltd.
 * SUNRISE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hotent.gzdc.kettle.qrtz.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import org.apache.log4j.Logger;

public class KettleConstants {
	private static Logger logger = Logger.getLogger(KettleConstants.class);
	public static Properties properties = new Properties();
	public static String KETTLE_USER_RESOURCE_TABLE_NAME = "KETTLE_USER_RESOURCE";
	
	public static String KETTLE_CACHE_FILE_TABLE_NAME = "KETTLE_CACHE_FILE";
	
	/**
	 * KETTLE SIMPLE JNDI DIRECTORY
	 */
	public static String KETTLE_SIMPLE_JNDI_DIRECTORY = "KETTLE_SIMPLE_JNDI_DIRECTORY";

	static {
		try {
			properties.load(KettleConstants.class.getResourceAsStream("/constants.properties"));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static String get(String key) {
		return properties.getProperty(key, "");
	}

	public static String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static void set(String key, String value) {
		try {
			Writer w = new FileWriter(KettleConstants.class.getResource("").getFile());
			properties.setProperty(key, value);
			properties.store(w, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
