package eu.the5zig.mod.chat;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.User;

import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class GroupMember extends User implements Comparable<GroupMember> {

	public static final int MEMBER = 0, ADMIN = 1, OWNER = 2;

	private int type;
	private int maxWidth = 220;

	public GroupMember(String username, UUID uuid, int type) {
		super(username, uuid);
		this.type = type;
	}

	public GroupMember(User user, int type) {
		this(user.getUsername(), user.getUniqueId(), type);
	}

	public boolean isAdmin() {
		return type == ADMIN;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	@Override
	public void draw(int x, int y) {
		String displayName = getUsername();
		if (maxWidth >= 150) {
			switch (getType()) {
				case ADMIN:
					displayName += String.format(" (%s)", I18n.translate("group.admin"));
					break;
				case OWNER:
					displayName += String.format(" (%s)", I18n.translate("group.owner"));
					break;
			}
		}
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(displayName, maxWidth - 4), x + 2, y + 2);
	}

	@Override
	public int compareTo(GroupMember other) {
		if (getType() == OWNER && other.getType() != OWNER)
			return -1;
		if (getType() != OWNER && other.getType() == OWNER)
			return 1;
		if (getType() == ADMIN && other.getType() != ADMIN)
			return -1;
		if (getType() != ADMIN && other.getType() == ADMIN)
			return 1;
		return getUsername().compareTo(other.getUsername());
	}
}
