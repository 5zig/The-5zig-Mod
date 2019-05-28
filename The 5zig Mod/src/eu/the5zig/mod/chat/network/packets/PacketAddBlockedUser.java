package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.User;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketAddBlockedUser implements Packet {

	private User blockedUser;

	public PacketAddBlockedUser(User blockedUser) {
		this.blockedUser = blockedUser;
	}

	public PacketAddBlockedUser() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.blockedUser = PacketBuffer.readUser(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUser(buffer, blockedUser);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getFriendManager().addBlockedUser(blockedUser);
	}
}
