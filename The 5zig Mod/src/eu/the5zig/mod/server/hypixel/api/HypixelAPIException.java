package eu.the5zig.mod.server.hypixel.api;

public class HypixelAPIException extends Exception {

	public HypixelAPIException() {
	}

	public HypixelAPIException(String message) {
		super(message);
	}

	public HypixelAPIException(String message, Throwable cause) {
		super(message, cause);
	}

	public HypixelAPIException(Throwable cause) {
		super(cause);
	}
}
