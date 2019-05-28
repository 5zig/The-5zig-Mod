package eu.the5zig.mod.chat.entity;

import eu.the5zig.util.minecraft.ChatColor;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Message implements Comparable<Message> {

	private final Conversation conversation;
	private int id;
	private String username;
	private String message;
	private long time;
	private MessageType messageType;

	public Message(Conversation conversation, int id, String username, String message, long time, MessageType messageType) {
		this.conversation = conversation;
		this.id = id;
		this.username = username;
		this.message = message;
		this.time = time;
		this.messageType = messageType;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String getUsername() {
		return username;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public long getTime() {
		return time;
	}

	@Override
	public int compareTo(Message o) {
		return Long.valueOf(getTime()).compareTo(o.getTime());
	}

	@Override
	public String toString() {
		return username + ChatColor.RESET + ": " + message;
	}

	public enum MessageStatus {
		PENDING, SENT, DELIVERED, READ
	}

	public enum MessageType {
		LEFT, RIGHT, CENTERED, DATE, IMAGE, AUDIO
	}

}
