package eu.the5zig.util.io;

import java.util.Properties;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PropertyLoader {

	private PropertyLoader() {
	}

	public static Properties load(String path) {
		try {
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
			return properties;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
