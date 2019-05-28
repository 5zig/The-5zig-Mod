package eu.the5zig.util.db.exceptions;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class NoConnectionException extends Exception {

	public NoConnectionException() {
	}

	public NoConnectionException(String message) {
		super(message);
	}

	public NoConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoConnectionException(Throwable cause) {
		super(cause);
	}
}
