package eu.the5zig.mod.gui.ts.menu;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.CenteredTextfieldCallback;
import eu.the5zig.mod.gui.GuiCenteredTextfield;
import eu.the5zig.mod.gui.ts.GuiTeamSpeakBanClient;
import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakClient;
import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakEntry;
import eu.the5zig.mod.util.Vector2i;
import eu.the5zig.teamspeak.TeamSpeak;
import eu.the5zig.teamspeak.api.ServerTab;

public class GuiTeamSpeakContextMenuClient extends GuiTeamSpeakContextMenuClientSelf {

	public GuiTeamSpeakContextMenuClient() {
		super();
		entries.set(0, new GuiTeamSpeakContextMenuEntry(new Vector2i(6, 1), "client.open_chat") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
				if (selectedTab != null) {
					selectedTab.getPrivateChat(((GuiTeamSpeakClient) entry).getClient());
				}
			}
		});
		entries.add(1, new GuiTeamSpeakContextMenuEntry(new Vector2i(12, 7), "client.poke") {
			@Override
			public void onClick(final GuiTeamSpeakEntry entry) {
				The5zigMod.getVars().displayScreen(new GuiCenteredTextfield(The5zigMod.getVars().getCurrentScreen(), new CenteredTextfieldCallback() {
					@Override
					public void onDone(String text) {
						if (text != null) {
							((GuiTeamSpeakClient) entry).getClient().poke(text);
						}
					}

					@Override
					public String title() {
						return I18n.translate("teamspeak.menu.client.poke.message");
					}
				}, 1000));
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(10, 5), "client.kick_channel") {
			@Override
			public void onClick(final GuiTeamSpeakEntry entry) {
				The5zigMod.getVars().displayScreen(new GuiCenteredTextfield(The5zigMod.getVars().getCurrentScreen(), new CenteredTextfieldCallback() {
					@Override
					public void onDone(String text) {
						if (text != null) {
							((GuiTeamSpeakClient) entry).getClient().kickFromChannel(text);
						}
					}

					@Override
					public String title() {
						return I18n.translate("teamspeak.menu.client.kick_channel.message");
					}
				}, -1, 1000));
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(11, 5), "client.kick_server") {
			@Override
			public void onClick(final GuiTeamSpeakEntry entry) {
				The5zigMod.getVars().displayScreen(new GuiCenteredTextfield(The5zigMod.getVars().getCurrentScreen(), new CenteredTextfieldCallback() {
					@Override
					public void onDone(String text) {
						if (text != null) {
							((GuiTeamSpeakClient) entry).getClient().kickFromServer(text);
						}
					}

					@Override
					public String title() {
						return I18n.translate("teamspeak.menu.client.kick_server.message");
					}
				}, -1, 1000));
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(11, 0), "client.ban") {
			@Override
			public void onClick(final GuiTeamSpeakEntry entry) {
				The5zigMod.getVars().displayScreen(new GuiTeamSpeakBanClient(The5zigMod.getVars().getCurrentScreen(), ((GuiTeamSpeakClient) entry).getClient()));
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(7, 5), "client.mute") {
			@Override
			public void onClick(final GuiTeamSpeakEntry entry) {
				((GuiTeamSpeakClient) entry).getClient().mute();
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(7, 5), "client.unmute") {
			@Override
			public void onClick(final GuiTeamSpeakEntry entry) {
				((GuiTeamSpeakClient) entry).getClient().unMute();
			}
		});
	}
}
