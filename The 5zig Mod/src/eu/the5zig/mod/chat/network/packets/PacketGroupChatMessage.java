package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Group;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketGroupChatMessage implements Packet {

	private int id;
	private String username;
	private String message;
	private long time;

	public PacketGroupChatMessage(int id, String message, long time) {
		this.id = id;
		this.message = message;
		this.time = time;
	}

	public PacketGroupChatMessage() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.id = PacketBuffer.readVarIntFromBuffer(buffer);
		this.username = PacketBuffer.readString(buffer);
		this.message = PacketBuffer.readString(buffer);
		this.time = buffer.readLong();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, id);
		PacketBuffer.writeString(buffer, message);
		buffer.writeLong(time);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		Group group = The5zigMod.getGroupChatManager().getGroup(id);
		if (group == null)
			return;
		if (The5zigMod.getConfig().getBool("playMessageSounds"))
			The5zigMod.getVars().playSound("the5zigmod", "chat.message.receive", 1);

		The5zigMod.getConversationManager().handleGroupChatMessage(group, username, message, time);
	}
}
