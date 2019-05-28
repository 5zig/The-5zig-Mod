package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketDisconnect implements Packet {

	private String reason;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.reason = PacketBuffer.readString(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {

	}

	@Override
	public void handle() {
		The5zigMod.getNetworkManager().disconnect(reason);
	}
}
