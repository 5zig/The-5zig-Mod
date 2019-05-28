import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.elements.ITextfield;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 * <p/>
 * Textfield class is a Simple Wrapper for default Minecraft Textfields.
 * Since I use obfuscated code, this textfield class simply refactors all required methods of the original Textfield.
 */
public class Textfield extends cgn implements ITextfield {

	/**
	 * ID, Width and height need to be declared in this class, since there is no direct access to it in original Textfield class.
	 */
	private final int id, width, height;

	/**
	 * Creates a new Textfield, using the original super constructor.
	 *
	 * @param id              The id of the Textfield. Currently, only used, to identify textfield by iterating though all textfields.
	 * @param x               The x-location of the Textfield.
	 * @param y               The y-location of the Textfield.
	 * @param width           The width of the Textfield
	 * @param height          The height of the Textfield.
	 * @param maxStringLength The Max Length the Text of the Textfield can have.
	 */
	public Textfield(int id, int x, int y, int width, int height, int maxStringLength) {
		super(id, ((Variables) MinecraftFactory.getVars()).getFontrenderer(), x, y, width, height);
		this.id = id;
		this.width = width;
		this.height = height;
		setMaxStringLength(maxStringLength);
	}

	/**
	 * Creates a new Textfield, using the original super constructor and 32 as max String length.
	 *
	 * @param id     The id of the Textfield. Currently, only used, to identify textfield by iterating though all textfields.
	 * @param x      The x-location of the Textfield.
	 * @param y      The y-location of the Textfield.
	 * @param width  The width of the Textfield
	 * @param height The height of the Textfield.
	 */
	public Textfield(int id, int x, int y, int width, int height) {
		this(id, x, y, width, height, 32);
	}

	/**
	 * Gets the id of the Textfield.
	 *
	 * @return The id of the Textfield.
	 */
	@Override
	public int getId() {
		return id;
	}

	/**
	 * Focuses this Textfield.
	 *
	 * @param selected if this Textfield should be focused or not.
	 */
	@Override
	public void setSelected(boolean selected) {
		b(selected);
	}

	/**
	 * Checks, if this Textfield is currently focused/selected.
	 *
	 * @return if this Textfield is currently focused.
	 */
	@Override
	public boolean isFocused() {
		return m();
	}

	/**
	 * Sets focus to this Textfield.
	 *
	 * @param focused if the Textfield should be focused.
	 */
	@Override
	public void setFocused(boolean focused) {
		b(focused);
	}

	/**
	 * Checks, if the Textfield is currently drawing its background texture.
	 *
	 * @return if the background of the Textfield is being drawed.
	 */
	@Override
	public boolean isBackgroundDrawing() {
		return i();
	}

	/**
	 * Gets the x-location of the Textfield.
	 *
	 * @return the x-location of the Textfield.
	 */
	@Override
	public int getX() {
		return a;
	}

	/**
	 * Sets the x-location of the Textfield.
	 *
	 * @param x The new x-location of the Textfield.
	 */
	@Override
	public void setX(int x) {
		this.a = x;
	}

	/**
	 * Gets the y-location of the Textfield.
	 *
	 * @return the y-location of the Textfield.
	 */
	@Override
	public int getY() {
		return f;
	}

	/**
	 * Sets the y-location of the Textfield.
	 *
	 * @param y The new y-location of the Textfield.
	 */
	@Override
	public void setY(int y) {
		this.f = y;
	}

	/**
	 * Gets the width of the Textfield.
	 *
	 * @return the width of the Textfield.
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the Textfield.
	 *
	 * @return the height of the Textfield.
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the max String Length the Text of the Textfield can have.
	 *
	 * @return the max String Length the Text of the Textfield can have.
	 */
	@Override
	public int getMaxStringLength() {
		return h();
	}

	/**
	 * Sets the max String Length the Text of the Textfield can have.
	 *
	 * @param length The max String length of the Text.
	 */
	@Override
	public void setMaxStringLength(int length) {
		f(length);
	}

	/**
	 * Gets the current Text of the Textfield.
	 *
	 * @return The current Text of the Textfield.
	 */
	@Override
	public String getText() {
		return b();
	}

	/**
	 * Sets the Text of the Textfield. Also checks if the text is larger, than getMaxStringLength().
	 *
	 * @param string The Text that should be put into the Textfield.
	 */
	@Override
	public void setText(String string) {
		a(string);
	}

	/**
	 * Simulates a Mouse click in the Textfield. Used in Gui when iterating through all textfields.
	 *
	 * @param x      The x-location of the Mouse.
	 * @param y      The y-location of the Mouse.
	 * @param button The button that has been pressed of the Mouse.
	 */
	@Override
	public void mouseClicked(int x, int y, int button) {
		super.mouseClicked(x, y, button);
	}

	@Override
	public boolean keyTyped(char character, int key) {
		return super.charTyped(character, key);
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {
		return super.keyPressed(key, scanCode, modifiers);
	}

	/**
	 * Used for blinking caret. Called from Gui when iterating through all textfields.
	 */
	@Override
	public void tick() {
		a();
	}

	/**
	 * Draws the Textfield. Called from Gui when iterating through all textfields.
	 */
	@Override
	public void draw() {
		a(0,0,0);
	}
}
