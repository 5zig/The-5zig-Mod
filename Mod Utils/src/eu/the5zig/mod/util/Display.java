package eu.the5zig.mod.util;

public class Display {

	private static DisplayHandler handler;

	public static void init(DisplayHandler handler) {
		Display.handler = handler;
	}

	public static boolean isActive() {
		return handler.isActive();
	}

	public interface DisplayHandler {
		boolean isActive();
	}

}
