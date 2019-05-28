package eu.the5zig.mod.gui.elements;

/**
 * Created by 5zig.
 * All rights reserved � 2015
 * <p/>
 * Textfield class is a Simple Wrapper for default Minecraft Textfields.
 * Since I use obfuscated code, this textfield class simply refactors all required methods of the original Textfield.
 */
public interface ITextfield {

	/**
	 * Gets the id of the Textfield.
	 *
	 * @return The id of the Textfield.
	 */
	int getId();

	/**
	 * Focuses this Textfield.
	 *
	 * @param selected if this Textfield should be focused or not.
	 */
	void setSelected(boolean selected);

	/**
	 * Checks, if this Textfield is currently focused/selected.
	 *
	 * @return if this Textfield is currently focused.
	 */
	boolean isFocused();

	/**
	 * Sets focus to this Textfield.
	 *
	 * @param focused if the Textfield should be focused.
	 */
	void setFocused(boolean focused);

	/**
	 * Checks, if the Textfield is currently drawing its background texture.
	 *
	 * @return if the background of the Textfield is being drawed.
	 */
	boolean isBackgroundDrawing();

	/**
	 * Gets the x-location of the Textfield.
	 *
	 * @return the x-location of the Textfield.
	 */
	int getX();

	/**
	 * Sets the x-location of the Textfield.
	 *
	 * @param x The new x-location of the Textfield.
	 */
	void setX(int x);

	/**
	 * Gets the y-location of the Textfield.
	 *
	 * @return the y-location of the Textfield.
	 */
	int getY();

	/**
	 * Sets the y-location of the Textfield.
	 *
	 * @param y The new y-location of the Textfield.
	 */
	void setY(int y);

	/**
	 * Gets the width of the Textfield.
	 *
	 * @return the width of the Textfield.
	 */
	int getWidth();

	/**
	 * Gets the height of the Textfield.
	 *
	 * @return the height of the Textfield.
	 */
	int getHeight();

	/**
	 * Gets the max String Length the Text of the Textfield can have.
	 *
	 * @return the max String Length the Text of the Textfield can have.
	 */
	int getMaxStringLength();

	/**
	 * Sets the max String Length the Text of the Textfield can have.
	 *
	 * @param length The max String length of the Text.
	 */
	void setMaxStringLength(int length);

	/**
	 * Gets the current Text of the Textfield.
	 *
	 * @return The current Text of the Textfield.
	 */
	String getText();

	/**
	 * Sets the Text of the Textfield. Also checks if the text is larger, than getMaxStringLength().
	 *
	 * @param string The Text that should be put into the Textfield.
	 */
	void setText(String string);

	/**
	 * Simulates a Mouse click in the Textfield. Used in Gui when iterating through all textfields.
	 *
	 * @param x      The x-location of the Mouse.
	 * @param y      The y-location of the Mouse.
	 * @param button The button that has been pressed of the Mouse.
	 */
	void mouseClicked(int x, int y, int button);

	/**
	 * Simulates a Key type in the Textfield. Used in Gui when iterating through all textfields.
	 *
	 * @param character The character that has been typed.
	 * @param key       The LWJGL-Integer of the typed key.
	 */
	boolean keyTyped(char character, int key);

	boolean keyPressed(int key, int scanCode, int modifiers);

	/**
	 * Used for blinking caret. Called from Gui when iterating through all textfields.
	 */
	void tick();

	/**
	 * Draws the Textfield. Called from Gui when iterating through all textfields.
	 */
	void draw();

}
