package eu.the5zig.mod.gui.ts.menu;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.ts.GuiTeamSpeak;
import eu.the5zig.mod.gui.ts.GuiTeamSpeakCreateChannel;
import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakEntry;
import eu.the5zig.mod.util.Vector2i;
import eu.the5zig.teamspeak.TeamSpeak;
import eu.the5zig.teamspeak.api.ServerTab;

public class GuiTeamSpeakContextMenuServer extends GuiTeamSpeakContextMenu {

	public GuiTeamSpeakContextMenuServer() {
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(9, 1), "server.create_channel") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				final ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
				if (selectedTab == null) {
					return;
				}
				The5zigMod.getVars().displayScreen(new GuiTeamSpeakCreateChannel(The5zigMod.getVars().getCurrentScreen(), null) {
					@Override
					protected void onDone(ChannelResult result) {
						selectedTab.createChannel(result.name, result.password, result.topic, result.description, result.lifespan, result.defaultChannel, null, result.orderChannel,
								result.bottomPosition, result.neededTalkPower, result.codec, result.codecQuality, result.maxClients);
					}
				});
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(7, 1), "server.collapse_all") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				GuiTeamSpeak.collapseAllChannels = true;
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(14, 1), "server.uncollapse_all") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				GuiTeamSpeak.collapsedChannels.clear();
			}
		});
	}
}
