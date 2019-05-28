package eu.the5zig.mod.chat.entity;

import java.util.UUID;

public class FriendSuggestion extends User {

	private boolean hide;

	public FriendSuggestion(String username, UUID uuid, boolean hide) {
		super(username, uuid);
		this.hide = hide;
	}

	public void setHidden(boolean hide) {
		this.hide = hide;
	}

	public boolean isHidden() {
		return hide;
	}
}
