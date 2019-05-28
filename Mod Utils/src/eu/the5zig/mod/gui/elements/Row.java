package eu.the5zig.mod.gui.elements;

public interface Row {

	/**
	 * Draws the row at given coordinates
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	void draw(int x, int y);

	int getLineHeight();

}