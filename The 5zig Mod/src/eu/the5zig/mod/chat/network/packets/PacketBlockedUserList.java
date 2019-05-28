package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.User;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketBlockedUserList implements Packet {

	private List<User> blockedUsers;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		int size = buffer.readInt();
		blockedUsers = new ArrayList<User>(size);
		for (int i = 0; i < size; i++) {
			User blockedUser = PacketBuffer.readUser(buffer);
			blockedUsers.add(blockedUser);
		}
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {

	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getFriendManager().setBlockedUsers(blockedUsers);
	}
}
