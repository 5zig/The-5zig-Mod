package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.IButton;

public class GuiDonatorSettings extends Gui {

	private String message;

	public GuiDonatorSettings(Gui lastScreen) {
		super(lastScreen);
	}

	@Override
	public void initGui() {
		addDoneButton();

		addButton(The5zigMod.getVars().createButton(1, getWidth() / 2 - 155, getHeight() / 6 - 6, 150, 20, I18n.translate("config.main.cape_settings")));
		addButton(The5zigMod.getVars().createButton(2, getWidth() / 2 + 5, getHeight() / 6 - 6, 150, 20, I18n.translate("config.main.item_model_settings")));
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 1) {
			The5zigMod.getVars().displayScreen(new GuiCapeSettings(this));
		}
	}

	@Override
	protected void tick() {
		super.tick();
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
