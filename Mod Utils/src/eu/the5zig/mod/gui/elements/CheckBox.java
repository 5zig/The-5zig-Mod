package eu.the5zig.mod.gui.elements;

import eu.the5zig.mod.MinecraftFactory;
import eu.the5zig.mod.gui.Gui;

public class CheckBox {

	private final int x;
	private final int y;
	private final int width;
	private final String string;

	private boolean selected;

	public CheckBox(int x, int y, int width, String string) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.string = string;
	}

	public void mouseClicked(int mouseX, int mouseY) {
		if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 10) {
			selected = !selected;
		}
	}

	public void draw() {
		MinecraftFactory.getVars().drawString(MinecraftFactory.getVars().shortenToWidth(string, width - 14), x + 14, y + 1);
		Gui.drawRect(x, y, x + 10, y + 10, 0xffffffff);
		if (selected) {
			Gui.drawRect(x + 1, y + 1, x + 9, y + 9, 0xff000000);
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}
}
