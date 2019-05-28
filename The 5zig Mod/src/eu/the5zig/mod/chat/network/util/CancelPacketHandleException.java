package eu.the5zig.mod.chat.network.util;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class CancelPacketHandleException extends RuntimeException {

	public static CancelPacketHandleException INSTANCE = new CancelPacketHandleException();

	private CancelPacketHandleException() {
		setStackTrace(new StackTraceElement[0]);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		this.setStackTrace(new StackTraceElement[0]);
		return this;
	}

}
