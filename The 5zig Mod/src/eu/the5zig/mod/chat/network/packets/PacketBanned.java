package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.GuiBanned;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketBanned implements Packet {

	private String reason;
	private long time;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.reason = PacketBuffer.readString(buffer);
		this.time = buffer.readLong();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		The5zigMod.getVars().displayScreen(new GuiBanned(reason, time));
	}
}
