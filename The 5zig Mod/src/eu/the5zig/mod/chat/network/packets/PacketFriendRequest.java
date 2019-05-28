package eu.the5zig.mod.chat.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketFriendRequest implements Packet {

	private String friend;

	public PacketFriendRequest(String friend) {
		this.friend = friend;
	}

	public PacketFriendRequest() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeString(buffer, friend);
	}

	@Override
	public void handle() {
	}
}
