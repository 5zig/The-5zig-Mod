package eu.the5zig.mod.gui;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.IButton;

import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class GuiYesNo extends Gui {

	private final YesNoCallback callback;

	public GuiYesNo(Gui lastScreen, YesNoCallback callback) {
		super(lastScreen);
		this.callback = callback;
	}

	@Override
	public void initGui() {
		addButton(The5zigMod.getVars().createButton(1, getWidth() / 2 - 152, getHeight() / 6 + 140, 150, 20, The5zigMod.getVars().translate("gui.yes")));
		addButton(The5zigMod.getVars().createButton(2, getWidth() / 2 + 2, getHeight() / 6 + 140, 150, 20, The5zigMod.getVars().translate("gui.no")));
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int maxStringWidth = getWidth() / 4 * 3;
		List<String> strings = The5zigMod.getVars().splitStringToWidth(callback.title(), maxStringWidth);
		int yOff = 0;
		for (String string : strings) {
			drawCenteredString(string, getWidth() / 2, getHeight() / 6 + (yOff += 12));
		}
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 1 || button.getId() == 2) {
			The5zigMod.getVars().displayScreen(lastScreen);
			callback.onDone(button.getId() == 1);
		}
	}

	@Override
	public String getTitleName() {
		return "";
	}
}
