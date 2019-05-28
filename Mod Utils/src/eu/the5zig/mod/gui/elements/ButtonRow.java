package eu.the5zig.mod.gui.elements;

import eu.the5zig.mod.MinecraftFactory;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ButtonRow implements RowExtended {

	public IButton button1;
	public IButton button2;

	public ButtonRow(IButton button1, IButton button2) {
		this.button1 = button1;
		this.button2 = button2;
	}

	@Override
	public void draw(int x, int y) {
	}

	@Override
	public void draw(int x, int y, int slotHeight, int mouseX, int mouseY) {
		if (button1 != null) {
			button1.setY(y + 2);
			button1.draw(mouseX, mouseY);
		}
		if (button2 != null) {
			button2.setY(y + 2);
			button2.draw(mouseX, mouseY);
		}
	}

	@Override
	public IButton mousePressed(int mouseX, int mouseY) {
		if (button1 != null) {
			if (button1.mouseClicked(mouseX, mouseY)) {
				button1.playClickSound();
				MinecraftFactory.getVars().getCurrentScreen().actionPerformed0(button1);
				return button1;
			}
		}
		if (button2 != null) {
			if (button2.mouseClicked(mouseX, mouseY)) {
				button2.playClickSound();
				MinecraftFactory.getVars().getCurrentScreen().actionPerformed0(button2);
				return button2;
			}
		}
		return null;
	}

	@Override
	public int getLineHeight() {
		return 24;
	}
}
