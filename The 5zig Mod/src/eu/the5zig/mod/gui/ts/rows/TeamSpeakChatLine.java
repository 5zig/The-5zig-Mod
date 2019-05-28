package eu.the5zig.mod.gui.ts.rows;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.gui.ChatLine;
import eu.the5zig.teamspeak.api.Message;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.List;

public class TeamSpeakChatLine extends ChatLine {

	private String messageContent;
	private final int width;

	public TeamSpeakChatLine(Message message, int width) {
		super(new eu.the5zig.mod.chat.entity.Message(null, 0, null, message.getMessage(), message.getTime(), null));
		LINE_HEIGHT = 10;
		MESSAGE_HEIGHT = 12;
		this.messageContent = message.getMessage();
		int errorIndex = this.messageContent.indexOf("An error occurred: ");
		if (errorIndex == 11) {
			this.messageContent = this.messageContent.substring(0, errorIndex) + ChatColor.RED + this.messageContent.substring(errorIndex, this.messageContent.length());
		}
		int urlStartIndex = this.messageContent.indexOf("[URL]");
		int urlEndIndex = this.messageContent.lastIndexOf("[/URL]");
		while (urlStartIndex != -1 && urlEndIndex != -1 && urlEndIndex > urlStartIndex) {
			String start = this.messageContent.substring(0, urlStartIndex);
			String middle = this.messageContent.substring(urlStartIndex + "[URL]".length(), urlEndIndex);
			String end = this.messageContent.substring(urlEndIndex + "[/URL]".length(), this.messageContent.length());
			this.messageContent = start + middle + end;
			urlStartIndex = this.messageContent.indexOf("[URL]");
			urlEndIndex = this.messageContent.lastIndexOf("[/URL]");
		}
		this.message.setMessage(this.messageContent);

		this.width = width;
	}

	@Override
	public void draw(int x, int y) {
		List<String> lines = The5zigMod.getVars().splitStringToWidth(messageContent, width);
		for (String line : lines) {
			The5zigMod.getVars().drawString(line, x, y);
			y += LINE_HEIGHT;
		}
	}

	@Override
	public int getLineHeight() {
		List<?> objects = The5zigMod.getVars().splitStringToWidth(messageContent, width);
		return (objects.size() - 1) * LINE_HEIGHT + MESSAGE_HEIGHT;
	}

	public String getMessageContent() {
		return messageContent;
	}

}
