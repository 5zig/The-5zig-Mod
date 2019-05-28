package eu.the5zig.mod.gui.elements;

public interface IButton {

	int getId();

	String getLabel();

	void setLabel(String label);

	int getWidth();

	void setWidth(int width);

	int getHeight();

	void setHeight(int height);

	boolean isEnabled();

	void setEnabled(boolean enabled);

	boolean isVisible();

	void setVisible(boolean visible);

	boolean isHovered();

	void setHovered(boolean hovered);

	int getX();

	void setX(int x);

	int getY();

	void setY(int y);

	void draw(int mouseX, int mouseY);

	void tick();

	boolean mouseClicked(int mouseX, int mouseY);

	void mouseReleased(int mouseX, int mouseY);

	void playClickSound();

	void setTicksDisabled(int ticks);

	void guiClosed();

}
