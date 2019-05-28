package eu.the5zig.mod.gui.ts;

import eu.the5zig.mod.gui.Gui;
import eu.the5zig.teamspeak.TeamSpeak;
import eu.the5zig.teamspeak.api.Channel;
import eu.the5zig.teamspeak.api.ChannelLifespan;
import eu.the5zig.teamspeak.api.ServerTab;

public class GuiTeamSpeakEditChannel extends GuiTeamSpeakCreateChannel {

	private final Channel channel;

	public GuiTeamSpeakEditChannel(Gui lastScreen, Channel channel) {
		super(lastScreen, channel.getParent());
		this.channel = channel;
		this.channelName = channel.getName();
		this.channelTopic = channel.getTopic();
		this.channelDescription = channel.getDescription();
		if (channel.isPermanent()) {
			channelLifespanIndex = ChannelLifespan.PERMANENT.ordinal();
		} else if (channel.isSemiPermanent()) {
			channelLifespanIndex = ChannelLifespan.SEMI_PERMANENT.ordinal();
		} else {
			channelLifespanIndex = ChannelLifespan.TEMPORARY.ordinal();
		}
		this.defaultChannelBool = channel.isDefault();
		this.currentCodecIndex = channel.getCodec().ordinal();
		this.codecQuality = channel.getCodecQuality();
		this.neededTalkPower = channel.getNeededTalkPower();
		this.maxClients = channel.getMaxClients();
		this.orderChannel = channel.getAbove();
	}

	@Override
	protected void onDone(ChannelResult result) {
		ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
		if (selectedTab == null) {
			return;
		}
		selectedTab.updateChannelProperties(channel, result.name, result.password, result.topic, result.description, result.lifespan, result.defaultChannel, result.parentChannel,
				result.orderChannel, result.bottomPosition, result.neededTalkPower, result.codec, result.codecQuality, result.maxClients);
	}
}
