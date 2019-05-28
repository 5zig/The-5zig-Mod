package eu.the5zig.mod.gui.ts.entries;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.teamspeak.api.Channel;

public class GuiTeamSpeakChannelSpacer extends GuiTeamSpeakChannel {

	private final String channelName;

	public GuiTeamSpeakChannelSpacer(Channel channel, String channelName) {
		super(channel);
		this.channelName = channelName;
	}

	@Override
	public void render(int x, int y, int width, int height) {
		String group = channelName;
		while (The5zigMod.getVars().getStringWidth(group + group.charAt(0)) < width) {
			group += group.charAt(0);
		}
		The5zigMod.getVars().drawString(group, x, y + 2);
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
