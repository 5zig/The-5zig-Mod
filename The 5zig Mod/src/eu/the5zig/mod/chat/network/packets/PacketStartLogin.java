package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketStartLogin implements Packet {

	private String username;

	private boolean offlineMode;
	private String key;

	public PacketStartLogin(String username) {
		this.username = username;
	}

	public PacketStartLogin() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.offlineMode = buffer.readBoolean();
		if (!offlineMode) {
			key = PacketBuffer.readString(buffer);
		}
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeString(buffer, username);
	}

	@Override
	public void handle() {
		if (!offlineMode) {
			The5zigMod.getNetworkManager().sendPacket(new PacketLogin(The5zigMod.getDataManager().getUsername(), The5zigMod.getDataManager().getUniqueId()));
		} else {
			The5zigMod.getNetworkManager().sendPacket(new PacketLogin(The5zigMod.getDataManager().getUsername(), The5zigMod.getDataManager().getUniqueId()));
		}
	}
}
