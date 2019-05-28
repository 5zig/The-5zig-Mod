package eu.the5zig.mod.gui.ts.menu;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.GuiYesNo;
import eu.the5zig.mod.gui.YesNoCallback;
import eu.the5zig.mod.gui.ts.GuiTeamSpeakCreateChannel;
import eu.the5zig.mod.gui.ts.GuiTeamSpeakEditChannel;
import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakChannel;
import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakEntry;
import eu.the5zig.mod.util.Vector2i;
import eu.the5zig.teamspeak.TeamSpeak;
import eu.the5zig.teamspeak.api.Channel;
import eu.the5zig.teamspeak.api.ServerTab;

public class GuiTeamSpeakContextMenuChannel extends GuiTeamSpeakContextMenu {

	public GuiTeamSpeakContextMenuChannel() {
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(5, 2), "channel.join") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
				if (selectedTab != null && selectedTab.getSelf() != null) {
					selectedTab.getSelf().joinChannel(((GuiTeamSpeakChannel) entry).getChannel());
				}
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(13, 1), "channel.edit") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				The5zigMod.getVars().displayScreen(new GuiTeamSpeakEditChannel(The5zigMod.getVars().getCurrentScreen(), ((GuiTeamSpeakChannel) entry).getChannel()));
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(12, 1), "channel.delete") {
			@Override
			public void onClick(final GuiTeamSpeakEntry entry) {
				The5zigMod.getVars().displayScreen(new GuiYesNo(The5zigMod.getVars().getCurrentScreen(), new YesNoCallback() {
					@Override
					public void onDone(boolean yes) {
						if (yes) {
							ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
							if (selectedTab == null) {
								return;
							}
							selectedTab.deleteChannel(((GuiTeamSpeakChannel) entry).getChannel(), true);
						}
					}

					@Override
					public String title() {
						return I18n.translate("teamspeak.menu.channel.delete.confirm");
					}
				}));
			}
		});
		entries.add(new GuiTeamSpeakContextMenuEntry(new Vector2i(10, 1), "channel.create_subchannel") {
			@Override
			public void onClick(GuiTeamSpeakEntry entry) {
				final ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
				if (selectedTab == null) {
					return;
				}
				final Channel parent = ((GuiTeamSpeakChannel) entry).getChannel();
				The5zigMod.getVars().displayScreen(new GuiTeamSpeakCreateChannel(The5zigMod.getVars().getCurrentScreen(), parent) {
					@Override
					protected void onDone(ChannelResult result) {
						selectedTab.createChannel(result.name, result.password, result.topic, result.description, result.lifespan, result.defaultChannel, result.parentChannel,
								result.orderChannel, result.bottomPosition, result.neededTalkPower, result.codec, result.codecQuality, result.maxClients);
					}
				});
			}
		});
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
						selectedTab.createChannel(result.name, result.password, result.topic, result.description, result.lifespan, result.defaultChannel, result.parentChannel,
								result.orderChannel, result.bottomPosition, result.neededTalkPower, result.codec, result.codecQuality, result.maxClients);
					}
				});
			}
		});
	}
}
