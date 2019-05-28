package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Friend;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketFriendList implements Packet {

	private List<Friend> friendList;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		int size = buffer.readInt();
		friendList = new ArrayList<Friend>(size);
		for (int i = 0; i < size; i++) {
			friendList.add(PacketBuffer.readFriend(buffer));
		}
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {

	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);
		The5zigMod.getFriendManager().setFriends(friendList);
	}
}
