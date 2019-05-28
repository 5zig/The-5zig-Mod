package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.util.minecraft.ChatColor;

public class GuiRaidCalculator extends GuiOptions {

	private String result = "";

	public GuiRaidCalculator(Gui lastScreen) {
		super(lastScreen);
	}

	@Override
	public void initGui() {
		addDoneButton();

		addTextField(The5zigMod.getVars().createTextfield(1, getWidth() / 2 - 140, 60, 80, 20));
		addTextField(The5zigMod.getVars().createTextfield(2, getWidth() / 2 - 40, 60, 80, 20));
		addTextField(The5zigMod.getVars().createTextfield(3, getWidth() / 2 + 60, 60, 80, 20));
		addButton(The5zigMod.getVars().createButton(12, getWidth() / 2 - 102, 90, 100, 20, I18n.translate("raid.track", I18n.translate("raid.east").toUpperCase())));
		addButton(The5zigMod.getVars().createButton(13, getWidth() / 2 + 2, 90, 100, 20, I18n.translate("raid.track", I18n.translate("raid.west").toUpperCase())));

		addTextField(The5zigMod.getVars().createTextfield(4, getWidth() / 2 - 140, 130, 80, 20));
		addTextField(The5zigMod.getVars().createTextfield(5, getWidth() / 2 - 40, 130, 80, 20));
		addTextField(The5zigMod.getVars().createTextfield(6, getWidth() / 2 + 60, 130, 80, 20));
		addButton(The5zigMod.getVars().createButton(10, getWidth() / 2 - 102, 160, 100, 20, I18n.translate("raid.track", I18n.translate("raid.north")).toUpperCase()));
		addButton(The5zigMod.getVars().createButton(11, getWidth() / 2 + 2, 160, 100, 20, I18n.translate("raid.track", I18n.translate("raid.south").toUpperCase())));

		for (int i = 1; i < getButtonList().size(); i++) {
			(getButtonList().get(i)).setEnabled(textfields.get(0).getText().length() > 0 && textfields.get(1).getText().length() > 0 && textfields.get(2).getText().length() > 0 &&
					isInt(textfields.get(0).getText()) && isInt(textfields.get(1).getText()) && isInt(textfields.get(2).getText()));
		}
	}

	@Override
	protected void actionPerformed(IButton button) {
		try {
			if (button.getId() == 10) {
				// north
				int z = Integer.parseInt(getTextfieldById(4).getText());
				int trackedZ = Integer.parseInt(getTextfieldById(5).getText());
				int narrowZ = Integer.parseInt(getTextfieldById(6).getText());
				int resZ;
				trackedZ = -trackedZ;

				resZ = z + trackedZ - narrowZ / 2;

				result = ChatColor.RESET + I18n.translate("raid.tracked", String.valueOf(trackedZ), String.valueOf(z), ChatColor.GOLD.toString() + resZ + ChatColor.RESET,
						ChatColor.DARK_RED + String.valueOf((double) resZ / 8.0) + ChatColor.RESET, I18n.translate("raid.north"));
			}
			if (button.getId() == 11) {
				// south
				int z = Integer.parseInt(getTextfieldById(4).getText());
				int trackedZ = Integer.parseInt(getTextfieldById(5).getText());
				int narrowZ = Integer.parseInt(getTextfieldById(6).getText());
				int resZ;

				resZ = z + trackedZ - narrowZ / 2;

				result = ChatColor.RESET + I18n.translate("raid.tracked", String.valueOf(trackedZ), String.valueOf(z), ChatColor.GOLD.toString() + resZ + ChatColor.RESET,
						ChatColor.DARK_RED + String.valueOf((double) resZ / 8.0) +
								ChatColor.RESET + I18n.translate("raid.south"));
			}
			if (button.getId() == 12) {
				// east
				int x = Integer.parseInt(getTextfieldById(1).getText());
				int trackedX = Integer.parseInt(getTextfieldById(2).getText());
				int narrowX = Integer.parseInt(getTextfieldById(3).getText());
				int resX;
				trackedX = -trackedX;

				resX = x + trackedX - narrowX / 2;

				result = ChatColor.RESET + I18n.translate("raid.tracked", String.valueOf(trackedX), String.valueOf(x), ChatColor.GOLD.toString() + resX + ChatColor.RESET,
						ChatColor.DARK_RED + String.valueOf((double) resX / 8.0) + ChatColor.RESET, I18n.translate("raid.east"));
			}
			if (button.getId() == 13) {
				// west
				int x = Integer.parseInt(getTextfieldById(1).getText());
				int trackedX = Integer.parseInt(getTextfieldById(2).getText());
				int narrowX = Integer.parseInt(getTextfieldById(3).getText());
				int resX;

				resX = x + trackedX - narrowX / 2;
				result = ChatColor.RESET + I18n.translate("raid.tracked", String.valueOf(trackedX), String.valueOf(x), ChatColor.GOLD.toString() + resX + ChatColor.RESET,
						ChatColor.DARK_RED + String.valueOf((double) resX / 8.0) +
								ChatColor.RESET, I18n.translate("raid.west"));
			}
		} catch (NumberFormatException ignored) {
		}
	}

	@Override
	protected void onKeyType(char character, int key) {
		for (int i = 1; i < 3; i++) {
			(getButtonList().get(i)).setEnabled(textfields.get(0).getText().length() > 0 && textfields.get(1).getText().length() > 0 && textfields.get(2).getText().length() > 0 &&
					isInt(textfields.get(0).getText()) && isInt(textfields.get(1).getText()) && isInt(textfields.get(2).getText()));
		}
		for (int i = 3; i < 5; i++) {
			(getButtonList().get(i)).setEnabled(textfields.get(3).getText().length() > 0 && textfields.get(4).getText().length() > 0 && textfields.get(5).getText().length() > 0 &&
					isInt(textfields.get(3).getText()) && isInt(textfields.get(4).getText()) && isInt(textfields.get(5).getText()));
		}
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawCenteredString(ChatColor.RED + result, getWidth() / 2, 190);

		The5zigMod.getVars().drawString("X: ", getWidth() / 2 - 155, 69);
		drawCenteredString(I18n.translate("raid.tracker_coords"), getWidth() / 2 - 100, 40);
		drawCenteredString(I18n.translate("raid.tracked_blocks"), getWidth() / 2, 40);
		drawCenteredString(I18n.translate("raid.narrow"), getWidth() / 2 + 100, 40);

		The5zigMod.getVars().drawString("Z: ", getWidth() / 2 - 155, 139);
	}

	@Override
	public String getTitleKey() {
		return "raid.title";
	}

}
