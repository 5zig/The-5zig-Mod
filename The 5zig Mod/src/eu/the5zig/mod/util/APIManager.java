package eu.the5zig.mod.util;

import eu.the5zig.mod.chat.network.NetworkManager;
import eu.the5zig.util.io.http.HttpClient;
import eu.the5zig.util.io.http.HttpResponseCallback;

public abstract class APIManager {

	private final String BASE_URL;

	public APIManager(String baseURL) {
		this.BASE_URL = baseURL;
	}

	/**
	 * Makes an async HTTP Request to {@link #BASE_URL}.
	 *
	 * @param endpoint The endpoint of the API.
	 * @param callback The Callback.
	 */
	protected void get(String endpoint, final HttpResponseCallback callback) throws Exception {
		if (NetworkManager.CLIENT_NIO_EVENTLOOP == null) {
			callback.call(null, 300, new RuntimeException("No NIO EventLoop"));
		} else {
			HttpClient.get(BASE_URL + endpoint, NetworkManager.CLIENT_NIO_EVENTLOOP, callback);
		}
	}

}
