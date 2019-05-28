package eu.the5zig.mod.chat.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Message;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.GuiConversations;
import eu.the5zig.mod.gui.GuiParty;
import eu.the5zig.util.Utils;

import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class DateChatLine extends CenteredChatLine {

	public final int LINE_HEIGHT = 8;
	public final int MESSAGE_HEIGHT = 14;
	private long time;

	public DateChatLine(Message message) {
		super(message);
		this.time = message.getTime();
	}

	@Override
	public void draw(int x, int y) {
		int yy = 2;
		float scale = 0.8f;
		String line = Utils.convertToDateWithoutTime(time).replace("Today", I18n.translate("profile.today")).replace("Yesterday", I18n.translate("profile.yesterday"));
		Gui.drawScaledString(line, x + 9 + getMaxMessageWidth() / 2 - (int) (The5zigMod.getVars().getStringWidth(line) * scale) / 2, y + yy, scale);
	}

	@Override
	public int getLineHeight() {
		List<?> objects = The5zigMod.getVars().splitStringToWidth(getMessage().getMessage(), getMaxMessageWidth());
		return (objects.size() - 1) * LINE_HEIGHT + MESSAGE_HEIGHT;
	}

	@Override
	public int getMaxMessageWidth() {
		if (The5zigMod.getVars().getCurrentScreen() instanceof GuiConversations) {
			return ((GuiConversations) The5zigMod.getVars().getCurrentScreen()).getChatBoxWidth() - 20;
		}
		if (The5zigMod.getVars().getCurrentScreen() instanceof GuiParty) {
			return ((GuiParty) The5zigMod.getVars().getCurrentScreen()).getChatBoxWidth() - 20;
		}
		return 100;
	}

}
