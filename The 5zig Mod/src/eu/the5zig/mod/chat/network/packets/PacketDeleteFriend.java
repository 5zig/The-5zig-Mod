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
public class PacketDeleteFriend implements Packet {

	private UUID friend;

	public PacketDeleteFriend(UUID friend) {
		this.friend = friend;
	}

	public PacketDeleteFriend() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.friend = PacketBuffer.readUUID(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUUID(buffer, friend);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getFriendManager().removeFriend(friend);
	}

}
