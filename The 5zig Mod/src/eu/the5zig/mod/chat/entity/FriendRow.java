package eu.the5zig.mod.chat.entity;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.Row;

public class FriendRow implements Row, Comparable<FriendRow> {

	public final Friend friend;

	public FriendRow(Friend friend) {
		this.friend = friend;
	}

	@Override
	public void draw(int x, int y) {
		The5zigMod.getVars().drawString(friend.getUsername(), x + 2, y + 2);
	}

	@Override
	public int getLineHeight() {
		return 18;
	}

	@Override
	public int compareTo(FriendRow o) {
		return friend.compareTo(o.friend);
	}
}
