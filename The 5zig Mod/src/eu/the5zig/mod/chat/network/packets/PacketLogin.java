package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.ConnectionState;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketLogin implements Packet {

	private String username;
	private UUID uuid;

	public PacketLogin(String username, UUID uuid) {
		this.username = username;
		this.uuid = uuid;
	}

	public PacketLogin() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {

	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeString(buffer, username);
		PacketBuffer.writeUUID(buffer, uuid);
	}

	@Override
	public void handle() {
		The5zigMod.getNetworkManager().setConnectState(ConnectionState.PLAY);
	}
}
