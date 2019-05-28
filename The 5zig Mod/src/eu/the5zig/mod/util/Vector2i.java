package eu.the5zig.mod.util;

/**
 * A two-dimensional vector.
 */
public class Vector2i {

	/**
	 * The x-coordinate of the vector.
	 */
	private int x;
	/**
	 * The y-coordinate of the vector.
	 */
	private int y;

	/**
	 * Creates a new instance of the vector.
	 *
	 * @param x the x-coordinate of the vector.
	 * @param y the y-coordinate of the vector.
	 */
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x-coordinate of the vector.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets a new x-coordinate.
	 *
	 * @param x the new x-coordinate.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y-coordinate of the vector.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets a new y-coordinate.
	 *
	 * @param y the new y-coordinate.
	 */
	public void setY(int y) {
		this.y = y;
	}
}
