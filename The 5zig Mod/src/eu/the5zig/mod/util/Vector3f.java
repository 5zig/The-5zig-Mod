package eu.the5zig.mod.util;

/**
 * A two-dimensional vector.
 */
public class Vector3f {

	/**
	 * The x-coordinate of the vector.
	 */
	private float x;
	/**
	 * The y-coordinate of the vector.
	 */
	private float y;
	/**
	 * The z-coordinate of the vector.
	 */
	private float z;

	/**
	 * Creates a new instance of the vector.
	 *
	 * @param x the x-coordinate of the vector.
	 * @param y the y-coordinate of the vector.
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @return the x-coordinate of the vector.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets a new x-coordinate.
	 *
	 * @param x the new x-coordinate.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y-coordinate of the vector.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets a new y-coordinate.
	 *
	 * @param y the new y-coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the z-coordinate of the vector.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets a new z-coordinate.
	 *
	 * @param z the new z-coordinate.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	public float distanceSquared(float x, float y, float z) {
		return (x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) + (z - this.z) * (z - this.z);
	}

	@Override
	public String toString() {
		return "Vector3f{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				'}';
	}
}
