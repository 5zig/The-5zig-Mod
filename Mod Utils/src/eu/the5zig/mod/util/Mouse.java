package eu.the5zig.mod.util;

public class Mouse {

	public static int BUTTON_LEFT = 0;
	public static int BUTTON_RIGHT = 1;
	public static int BUTTON_MIDDLE = 2;

	private static MouseHandler handler;

	public static void init(MouseHandler handler) {
		Mouse.handler = handler;
	}

	public static boolean isButtonDown(int button) {
		return handler.isButtonDown(button);
	}

	public static int getX() {
		return handler.getX();
	}

	public static int getY() {
		return handler.getY();
	}

	public interface MouseHandler {
		boolean isButtonDown(int button);

		int getX();

		int getY();
	}

}
