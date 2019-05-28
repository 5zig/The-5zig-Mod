package eu.the5zig.util.io.http;

public interface HttpResponseCallback {

	void call(String response, int responseCode, Throwable throwable);

}
