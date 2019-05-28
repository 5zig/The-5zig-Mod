package eu.the5zig.mod.util;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class IOUtil {

	private static final Gson gson = new Gson();

	public static <T> T downloadToJson(String url, boolean post, Class<T> jsonClass) throws IOException {
		return gson.fromJson(download(url, post), jsonClass);
	}

	public static String download(String url, boolean post) throws IOException {
		InputStream inputStream = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod(post ? "POST" : "GET");
			connection.setConnectTimeout(20000);
			connection.setReadTimeout(60000);
			inputStream = connection.getInputStream();
			return IOUtils.toString(inputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

}
