package eu.the5zig.mod.gui.ts.entries;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.CenteredTextfieldCallback;
import eu.the5zig.mod.gui.GuiCenteredTextfield;
import eu.the5zig.teamspeak.TeamSpeak;
import eu.the5zig.teamspeak.api.Client;
import eu.the5zig.teamspeak.api.ServerTab;

public class GuiTeamSpeakClientSelf extends GuiTeamSpeakClient {

	public GuiTeamSpeakClientSelf(Client client, String serverUniqueId) {
		super(client, serverUniqueId);
	}

	@Override
	public void onClick(boolean doubleClick) {
		if (doubleClick) {
			final ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
			if (selectedTab != null && selectedTab.getSelf() != null) {
				The5zigMod.getVars().displayScreen(new GuiCenteredTextfield(The5zigMod.getVars().getCurrentScreen(), new CenteredTextfieldCallback() {
					@Override
					public void onDone(String text) {
						if (text != null) {
							selectedTab.getSelf().setNickName(text);
						}
					}

					@Override
					public String title() {
						return I18n.translate("teamspeak.enter_nickname");
					}
				}, selectedTab.getSelf().getName() == null ? "" : selectedTab.getSelf().getName()));
			}
		}
	}

}
