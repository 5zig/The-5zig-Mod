package eu.the5zig.mod;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {

	public static final String VERSION;
	public static final String MCVERSION;
	public static final int LANGVERSION = 44;
	public static final int PROTOCOL = 5;
	public static final int APIVERSION = 4;

	static {
		String version = "DEV", mcVersion = "unknown";

		Properties properties = new Properties();
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("the5zigmod.properties");
			properties.load(in);
			version = properties.getProperty("version");
			mcVersion = properties.getProperty("mcversion");
		} catch (Exception e) {
			System.err.println("Could not load version details of the 5zig mod! Corrupted file or development environment?!");
		} finally {
			VERSION = version;
			MCVERSION = mcVersion;
			if (in != null) {
				try {
					in.close();
				} catch (IOException ignored) {
				}
			}
		}
	}

}