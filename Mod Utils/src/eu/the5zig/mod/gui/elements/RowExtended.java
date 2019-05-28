package eu.the5zig.mod.gui.elements;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public interface RowExtended extends Row {

	/**
	 * Draws the row at given coordinates
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	void draw(int x, int y, int slotHeight, int mouseX, int mouseY);

	/**
	 * Called when the mouse has been pressed.
	 *
	 * @param mouseX The x coordinate of the Mouse.
	 * @param mouseY The y coordinate of the Mouse.
	 * @return The element that has been pressed.
	 */
	IButton mousePressed(int mouseX, int mouseY);

}
