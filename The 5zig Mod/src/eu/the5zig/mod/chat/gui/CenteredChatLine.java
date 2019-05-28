package eu.the5zig.mod.chat.gui;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Message;
import eu.the5zig.mod.gui.GuiConversations;
import eu.the5zig.mod.gui.GuiParty;

import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class CenteredChatLine extends ChatLine {

	public CenteredChatLine(Message message) {
		super(message);
	}

	@Override
	public void draw(int x, int y) {
		List<?> lines = The5zigMod.getVars().splitStringToWidth(getMessage().getMessage(), getMaxMessageWidth());
		int yy = 2;
		for (Object object : lines) {
			String line = String.valueOf(object);
			The5zigMod.getVars().drawString(line, x + 9 + getMaxMessageWidth() / 2 - The5zigMod.getVars().getStringWidth(line) / 2, y + yy);
			yy += LINE_HEIGHT;
		}
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
