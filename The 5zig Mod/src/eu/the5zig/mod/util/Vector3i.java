package eu.the5zig.mod.util;

/**
 * A two-dimensional vector.
 */
public class Vector3i {

	/**
	 * The x-coordinate of the vector.
	 */
	private int x;
	/**
	 * The y-coordinate of the vector.
	 */
	private int y;
	/**
	 * The z-coordinate of the vector.
	 */
	private int z;

	/**
	 * Creates a new instance of the vector.
	 *
	 * @param x the x-coordinate of the vector.
	 * @param y the y-coordinate of the vector.
	 */
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	/**
	 * @return the z-coordinate of the vector.
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Sets a new z-coordinate.
	 *
	 * @param z the new z-coordinate.
	 */
	public void setZ(int z) {
		this.z = z;
	}

	public double distanceSquared(int x, int y, int z) {
		return (double) (x - this.x) * (double) (x - this.x) + (double) (y - this.y) * (double) (y - this.y) + (double) (z - this.z) * (double) (z - this.z);
	}

	@Override
	public String toString() {
		return "Vector3i{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}
}
