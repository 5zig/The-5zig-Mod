package eu.the5zig.mod.server.hypixel.api;

public class HypixelAPIResponseException extends HypixelAPIException {

	private int errorCode;
	private String errorMessage;

	public HypixelAPIResponseException(int errorCode, String errorMessage) {
		super("Could not fetch data from API: " + errorMessage + " (error code " + errorCode + ")");
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public HypixelAPIResponseException(String message) {
		super("Could not fetch data from API: " + message);
		this.errorCode = -1;
		this.errorMessage = message;
	}

	public HypixelAPIResponseException(Throwable cause) {
		this(cause.toString());
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
