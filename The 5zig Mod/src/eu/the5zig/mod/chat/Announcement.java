package eu.the5zig.mod.chat;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Announcement {

	private long time;
	private String message;

	public Announcement(long time, String message) {
		this.time = time;
		this.message = message;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
