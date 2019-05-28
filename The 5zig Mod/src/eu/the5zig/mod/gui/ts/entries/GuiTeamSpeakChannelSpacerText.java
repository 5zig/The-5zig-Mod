package eu.the5zig.mod.gui.ts.entries;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.teamspeak.api.Channel;

public class GuiTeamSpeakChannelSpacerText extends GuiTeamSpeakChannel {

	private final String channelName;

	public GuiTeamSpeakChannelSpacerText(Channel channel, String channelName) {
		super(channel);
		this.channelName = channelName;
	}

	@Override
	public void render(int x, int y, int width, int height) {
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(channelName, width), x, y + 2);
	}

	@Override
	public void renderIcons(int x, int y, int width, int height) {
	}

	@Override
	public void renderDragging(int x, int y, int width, int height) {
		render(x, y, width, height);
	}

	@Override
	public void renderDraggingIcons(int x, int y, int width, int height) {
	}

	@Override
	public boolean canBeCollapsed() {
		return false;
	}
}
