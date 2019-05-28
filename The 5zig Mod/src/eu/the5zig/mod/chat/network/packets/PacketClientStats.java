package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.Version;
import eu.the5zig.util.Utils;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketClientStats implements Packet {

	@Override
	public void read(ByteBuf buffer) throws IOException {
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeString(buffer, Version.VERSION);
		PacketBuffer.writeString(buffer, Version.MCVERSION);
		PacketBuffer.writeString(buffer, Utils.getOSName());
		PacketBuffer.writeString(buffer, Utils.getJava());
		PacketBuffer.writeString(buffer, Locale.getDefault().toString());
	}

	@Override
	public void handle() {
		The5zigMod.getNetworkManager().sendPacket(new PacketClientStats());
	}
}
