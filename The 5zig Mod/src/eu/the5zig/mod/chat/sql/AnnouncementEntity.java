package eu.the5zig.mod.chat.sql;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class AnnouncementEntity {

	private int id;
	private long lastused;
	private boolean read;
	private int behaviour;

	public int getId() {
		return id;
	}

	public long getLastused() {
		return lastused;
	}

	public boolean isRead() {
		return read;
	}

	public int getBehaviour() {
		return behaviour;
	}
}
