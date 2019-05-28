package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.items.StringItem;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.teamspeak.TeamSpeak;
import eu.the5zig.util.Utils;

public class GuiTeamSpeakAuth extends Gui {

	public GuiTeamSpeakAuth(Gui lastScreen) {
		super(lastScreen);

		selectTextFieldAfterInit = false;
	}

	@Override
	public void initGui() {
		addButton(The5zigMod.getVars().createButton(100, getWidth() / 2 + 2, getHeight() - 32, 150, 20, The5zigMod.getVars().translate("gui.done")));
		addButton(The5zigMod.getVars().createButton(200, getWidth() / 2 - 152, getHeight() - 32, 150, 20, The5zigMod.getVars().translate("gui.cancel")));

		addTextField(The5zigMod.getVars().createTextfield("AAAA-BBBB-CCCC-DDDD-EEEE-FFFF", 1, getWidth() / 2 - 80, getHeight() / 2 + 50, 180, 20, 29));
		String key = The5zigMod.getConfig().getString("tsAuthKey");
		if (key != null) {
			getTextfieldById(1).setText(key);
		}
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 100) {
			String text = getTextfieldById(1).getText();
			StringItem configItem = (StringItem) The5zigMod.getConfig().get("tsAuthKey");
			configItem.set(text);
			The5zigMod.getConfig().save();
			The5zigMod.getDataManager().setTsRequiresAuth(false);
			The5zigMod.getVars().displayScreen(lastScreen);
			TeamSpeak.getClient().disconnect();
			The5zigMod.getDataManager().getTeamSpeakReconnectListener().reconnectNow();
		}
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int y = getHeight() / 2 - 60;
		if (The5zigMod.getDataManager().isTsRequiresAuth()) {
			if (The5zigMod.getConfig().getString("tsAuthKey") == null) {
				for (String s : The5zigMod.getVars().splitStringToWidth(I18n.translate("teamspeak.auth.info"), getWidth() - 40)) {
					The5zigMod.getVars().drawCenteredString(s, getWidth() / 2, y);
					y += 12;
				}
			} else {
				for (String s : The5zigMod.getVars().splitStringToWidth(I18n.translate("teamspeak.auth.invalid_key"), getWidth() - 40)) {
					The5zigMod.getVars().drawCenteredString(s, getWidth() / 2, y);
					y += 12;
				}
			}
		}
		y += 12;
		for (String s : The5zigMod.getVars().splitStringToWidth(I18n.translate(Utils.getPlatform() == Utils.Platform.MAC ? "teamspeak.auth.steps.mac" : "teamspeak.auth.steps.win"), getWidth() - 40)) {
			The5zigMod.getVars().drawCenteredString(s, getWidth() / 2, y);
			y += 12;
		}
	}

	@Override
	public String getTitleKey() {
		return "teamspeak.auth.title";
	}
}
