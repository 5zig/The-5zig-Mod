package eu.the5zig.mod.gui.ts;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.teamspeak.api.Client;
import eu.the5zig.util.Utils;

public class GuiTeamSpeakBanClient extends Gui {

	private final Client client;

	public GuiTeamSpeakBanClient(Gui lastScreen, Client client) {
		super(lastScreen);
		this.client = client;
	}

	@Override
	public void initGui() {
		addButton(The5zigMod.getVars().createButton(100, getWidth() / 2 + 5, getHeight() / 6 + 168, 150, 20, The5zigMod.getVars().translate("gui.done")));
		addButton(The5zigMod.getVars().createButton(200, getWidth() / 2 - 155, getHeight() / 6 + 168, 150, 20, The5zigMod.getVars().translate("gui.cancel")));

		addTextField(The5zigMod.getVars().createTextfield(100, getWidth() / 2 - 100, getHeight() / 6 + 40, 250, 18, 1000));
		addTextField(The5zigMod.getVars().createTextfield(101, getWidth() / 2 - 100, getHeight() / 6 + 75, 100, 18, 1000));
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 100) {
			String reason = getTextfieldById(100).getText();
			int time = Utils.parseInt(getTextfieldById(101).getText());
			client.banFromServer(reason, time);

			The5zigMod.getVars().displayScreen(lastScreen);
		}
	}

	@Override
	protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
		The5zigMod.getVars().drawString(I18n.translate("teamspeak.ban_client.name"), getWidth() / 2 - 150, getHeight() / 6 + 18);
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(client.getName(), 250), getWidth() / 2 - 100, getHeight() / 6 + 18);
		The5zigMod.getVars().drawString(I18n.translate("teamspeak.ban_client.reason"), getWidth() / 2 - 150, getHeight() / 6 + 45);
		The5zigMod.getVars().drawString(I18n.translate("teamspeak.ban_client.duration"), getWidth() / 2 - 150, getHeight() / 6 + 80);
		The5zigMod.getVars().drawString(I18n.translate("teamspeak.ban_client.seconds"), getWidth() / 2 + 5, getHeight() / 6 + 80);
	}

	@Override
	public String getTitleKey() {
		return "teamspeak.ban_client.title";
	}
}
