package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketDeleteBlockedUser implements Packet {

	private UUID blockedUser;

	public PacketDeleteBlockedUser(UUID blockedUser) {
		this.blockedUser = blockedUser;
	}

	public PacketDeleteBlockedUser() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.blockedUser = PacketBuffer.readUUID(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUUID(buffer, blockedUser);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getFriendManager().removeBlockedUser(blockedUser);
	}
}
