package eu.the5zig.mod.chat.network.packets;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketFriendRequestResponse implements Packet {

	private UUID friend;
	private boolean accepted;

	public PacketFriendRequestResponse(UUID friend, boolean accepted) {
		this.friend = friend;
		this.accepted = accepted;
	}

	public PacketFriendRequestResponse() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {

	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUUID(buffer, friend);
		buffer.writeBoolean(accepted);
	}

	@Override
	public void handle() {

	}
}
