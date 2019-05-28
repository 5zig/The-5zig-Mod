package eu.the5zig.mod.gui.ts;

import com.google.common.collect.Lists;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.BasicRow;
import eu.the5zig.mod.gui.elements.Clickable;
import eu.the5zig.mod.gui.elements.IButton;
import eu.the5zig.mod.gui.elements.IGuiList;
import eu.the5zig.teamspeak.TeamSpeak;
import eu.the5zig.teamspeak.api.Channel;
import eu.the5zig.teamspeak.api.ServerTab;

import java.util.List;

public abstract class GuiTeamSpeakSelectChannelOrder extends Gui {

	private final Channel parentChannel;

	private IGuiList<ChannelRow> guiList;

	public GuiTeamSpeakSelectChannelOrder(Gui lastScreen, Channel parentChannel) {
		super(lastScreen);
		this.parentChannel = parentChannel;
	}

	@Override
	public void initGui() {
		addBottomDoneButton();

		guiList = The5zigMod.getVars().createGuiList(new Clickable<ChannelRow>() {
			@Override
			public void onSelect(int id, ChannelRow row, boolean doubleClick) {
				if (doubleClick) {
					actionPerformed0(getButtonById(200));
				}
			}
		}, getWidth(), getHeight(), 48, getHeight() - 48, 0, getWidth(), Lists.<ChannelRow>newArrayList());
		guiList.setRowWidth(200);
		addGuiList(guiList);
	}

	@Override
	protected void actionPerformed(IButton button) {
		if (button.getId() == 200) {
			onSelect(guiList.getSelectedRow().channel);
		}
	}

	protected abstract void onSelect(Channel channel);

	@Override
	protected void tick() {
		List<? extends Channel> channels;
		ServerTab selectedTab = TeamSpeak.getClient().getSelectedTab();
		if (selectedTab == null) {
			The5zigMod.getVars().displayScreen(lastScreen);
			return;
		}
		if (parentChannel != null) {
			channels = parentChannel.getChildren();
		} else {
			channels = selectedTab.getChannels();
		}
		guiList.getRows().clear();
		String topName = parentChannel == null ? selectedTab.getServerInfo().getName() : parentChannel.getName();
		guiList.getRows().add(new ChannelRow(topName, parentChannel));
		for (Channel channel : channels) {
			guiList.getRows().add(new ChannelRow(channel.getName(), channel));
		}
	}

	@Override
	public String getTitleKey() {
		return "teamspeak.create_channel.select_order.title";
	}

	private class ChannelRow extends BasicRow {

		private Channel channel;

		public ChannelRow(String name, Channel channel) {
			super(name, 200);
			this.channel = channel;
		}
	}
}
