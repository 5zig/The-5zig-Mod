package eu.the5zig.mod.gui.ts.menu;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.CenteredTextfieldCallback;
import eu.the5zig.mod.gui.GuiCenteredTextfield;
import eu.the5zig.mod.gui.ts.GuiTeamSpeakClientChannelGroups;
import eu.the5zig.mod.gui.ts.GuiTeamSpeakClientServerGroups;
import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakClient;
import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakEntry;
import eu.the5zig.mod.util.Vector2i;
import eu.the5zig.teamspeak.TeamSpeak;
import eu.the5zig.teamspeak.api.Client;
import eu.the5zig.teamspeak.api.ServerTab;

public class GuiTeamSpeakContextMenuClientSelf extends GuiTeamSpeakContextMenu {

	public GuiTeamSpeakContextMenuClientSelf() {
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(4, 1), "client.change_nickname") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
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
					}, selectedTab.getSelf().getName()));
				}
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(12, 6), "client.server_groups") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				final ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
				if (selectedTab != null) {
					Client client = ((GuiTeamSpeakClient) entry).getClient();
					The5zigMod.getVars().displayScreen(new GuiTeamSpeakClientServerGroups(The5zigMod.getVars().getCurrentScreen(), client));
				}
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(9, 6), "client.channel_group") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				final ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
				if (selectedTab != null) {
					Client client = ((GuiTeamSpeakClient) entry).getClient();
					The5zigMod.getVars().displayScreen(new GuiTeamSpeakClientChannelGroups(The5zigMod.getVars().getCurrentScreen(), client));
				}
			}
		});
	}
}
