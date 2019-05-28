package eu.the5zig.mod.chat.entity;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.util.minecraft.ChatColor;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ConversationGroupChat extends Conversation {

	private final int groupId;
	private String name;

	/**
	 * Creates a new conversation instance of a group chat
	 *
	 * @param conversationId The id of the conversation
	 * @param groupId        The id of the group
	 * @param lastUsed       The time when it has been last used.
	 * @param read           If the conversation has been read.
	 */
	public ConversationGroupChat(int conversationId, int groupId, String name, long lastUsed, boolean read, Message.MessageStatus status, Behaviour behaviour) {
		super(conversationId, lastUsed, read, status, behaviour);
		this.groupId = groupId;
		this.name = name;
	}

	public int getGroupId() {
		return groupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getLineHeight() {
		return 18;
	}

	@Override
	public void draw(int x, int y) {
		String displayName = name;
		boolean changed = false;
		while (The5zigMod.getVars().getStringWidth((!isRead() ? ChatColor.BOLD : "") + displayName) > 92 - The5zigMod.getVars().getStringWidth("...")) {
			displayName = displayName.substring(0, displayName.length() - 1);
			changed = true;
		}
		if (changed)
			displayName += "...";
		The5zigMod.getVars().drawString((isRead() ? "" : ChatColor.BOLD) + displayName, x + 2, y + 2);
	}
}
