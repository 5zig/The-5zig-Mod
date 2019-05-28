package eu.the5zig.mod.chat.sql;

import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ChatEntity {

	private int id;
	private UUID uuid;
	private String friend;
	private long lastused;
	private boolean read;
	private int status;
	private int behaviour;

	public int getId() {
		return id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}

	public long getLastUsed() {
		return lastused;
	}

	public boolean isRead() {
		return read;
	}

	public int getStatus() {
		return status;
	}

	public int getBehaviour() {
		return behaviour;
	}
}
