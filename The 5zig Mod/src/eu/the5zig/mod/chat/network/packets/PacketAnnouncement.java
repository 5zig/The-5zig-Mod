package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketAnnouncement implements Packet {

	private String message;
	private long time;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.message = PacketBuffer.readString(buffer);
		this.time = buffer.readLong();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {

	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getConversationManager().addAnnouncementMessage(message, time);
	}
}
