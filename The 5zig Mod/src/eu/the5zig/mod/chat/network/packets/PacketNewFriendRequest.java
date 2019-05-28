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
public class PacketNewFriendRequest implements Packet {

	private User friendRequest;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.friendRequest = PacketBuffer.readUser(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUser(buffer, friendRequest);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);
		The5zigMod.getFriendManager().addFriendRequest(friendRequest);
	}
}
