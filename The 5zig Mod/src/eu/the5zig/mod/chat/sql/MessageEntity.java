package eu.the5zig.mod.chat.sql;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class MessageEntity {

	private int id;
	private int conversationid;
	private String player;
	private String message;
	private long time;
	private int type;

	public int getId() {
		return id;
	}

	public int getConversationid() {
		return conversationid;
	}

	public String getPlayer() {
		return player;
	}

	public String getMessage() {
		return message;
	}

	public long getTime() {
		return time;
	}

	public int getType() {
		return type;
	}
}
