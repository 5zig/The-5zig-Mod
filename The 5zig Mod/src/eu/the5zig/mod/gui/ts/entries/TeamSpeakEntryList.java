package eu.the5zig.mod.gui.ts.entries;

import com.google.common.collect.Lists;

import java.util.List;

public class TeamSpeakEntryList {

	private List<GuiTeamSpeakEntry> entries = Lists.newArrayList();
	private int channelCount;
	private int clientCount;

	public List<GuiTeamSpeakEntry> getEntries() {
		return entries;
	}

	public int getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(int channelCount) {
		this.channelCount = channelCount;
	}

	public int getClientCount() {
		return clientCount;
	}

	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
}
