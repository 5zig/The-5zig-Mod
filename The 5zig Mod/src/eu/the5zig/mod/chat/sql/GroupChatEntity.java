package eu.the5zig.mod.chat.sql;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class GroupChatEntity {

	private int id;
	private int groupId;
	private String name;
	private long lastused;
	private boolean read;
	private int status;
	private int behaviour;

	public int getId() {
		return id;
	}

	public int getGroupId() {
		return groupId;
	}

	public String getName() {
		return name;
	}

	public long getLastused() {
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
