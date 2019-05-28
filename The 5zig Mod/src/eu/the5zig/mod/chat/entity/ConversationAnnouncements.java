package eu.the5zig.mod.chat.entity;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.util.minecraft.ChatColor;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ConversationAnnouncements extends Conversation {

	/**
	 * Creates a new conversation instance of an announcement
	 *
	 * @param conversationId The id of the conversation
	 * @param lastUsed       The time when it has been last used.
	 * @param read           If the announcement has been read.
	 */
	public ConversationAnnouncements(int conversationId, long lastUsed, boolean read, Behaviour behaviour) {
		super(conversationId, lastUsed, read, Message.MessageStatus.PENDING, behaviour);
	}

	@Override
	public int getLineHeight() {
		return 18;
	}

	@Override
	public void draw(int x, int y) {
		The5zigMod.getVars().drawString((isRead() ? "" : ChatColor.BOLD) + I18n.translate("announcement.short_desc"), x + 2, y + 2);
	}

}
