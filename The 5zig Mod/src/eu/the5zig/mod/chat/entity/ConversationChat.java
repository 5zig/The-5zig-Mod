package eu.the5zig.mod.chat.entity;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ConversationChat extends Conversation {

	private final UUID friendUUID;
	private String friendName;

	/**
	 * Creates a new conversation instance of a normal chat
	 *
	 * @param conversationId The id of the conversation
	 * @param friendName     The name of the friend
	 * @param friendUUID     The uuid of the friend
	 * @param lastUsed       The time when it has been last used.
	 * @param read           If the announcement has been read.
	 */
	public ConversationChat(int conversationId, String friendName, UUID friendUUID, long lastUsed, boolean read, Message.MessageStatus status, Behaviour behaviour) {
		super(conversationId, lastUsed, read, status, behaviour);
		this.friendName = friendName;
		this.friendUUID = friendUUID;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public UUID getFriendUUID() {
		return friendUUID;
	}

	@Override
	public int getLineHeight() {
		return 18;
	}

	@Override
	public void draw(int x, int y) {
		The5zigMod.getVars().drawString((isRead() ? "" : ChatColor.BOLD) + friendName, x + 2, y + 2);
	}
}
