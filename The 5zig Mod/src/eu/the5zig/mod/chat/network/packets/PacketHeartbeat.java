package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketHeartbeat implements Packet {

	private int id;

	public PacketHeartbeat(int id) {
		this.id = id;
	}

	public PacketHeartbeat() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.id = buffer.readInt();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		buffer.writeInt(id);
	}

	@Override
	public void handle() {
		The5zigMod.getNetworkManager().getHeartbeatManager().onHeartbeatReceive(id);
	}
}
