package eu.the5zig.mod.chat.sql;

public class FriendSuggestionEntity {

	private int id;
	private String uuid;
	private String name;
	private boolean hide;

	public int getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public boolean isHide() {
		return hide;
	}
}
