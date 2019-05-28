package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.ConnectionState;
import eu.the5zig.mod.chat.network.Protocol;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketHandshake implements Packet {

	@Override
	public void read(ByteBuf buffer) throws IOException {

	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, Protocol.VERSION);
	}

	@Override
	public void handle() {
		The5zigMod.getNetworkManager().setConnectState(ConnectionState.LOGIN);
		The5zigMod.getNetworkManager().sendPacket(new PacketStartLogin(The5zigMod.getDataManager().getUsername()));
	}
}
