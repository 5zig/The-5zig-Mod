package eu.the5zig.mod.util;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipFile;

public class EnvironmentUtils {

	private EnvironmentUtils() {
	}

	public static ZipFile getModFile() {
		try {
			URLClassLoader ucl = (URLClassLoader) Thread.currentThread().getContextClassLoader();

			URL[] urls = ucl.getURLs();
			for (URL url : urls) {
				ZipFile zipFile = getZipFile(url);
				if (zipFile != null) {
					return zipFile;
				}
			}
		} catch (Exception ignored) {
		}
		return null;
	}

	private static ZipFile getZipFile(URL url) {
		try {
			URI uri = url.toURI();

			File file = new File(uri);

			ZipFile zipFile = new ZipFile(file);
			if (zipFile.getEntry("eu/the5zig/mod/The5zigMod.class") == null) {
				zipFile.close();
				return null;
			}
			return zipFile;
		} catch (Exception ignored) {
			return null;
		}
	}

}
