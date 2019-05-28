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
public class PacketGroupBroadcast implements Packet {

	private int groupId;
	private String message;
	private long time;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.groupId = PacketBuffer.readVarIntFromBuffer(buffer);
		this.message = PacketBuffer.readString(buffer);
		this.time = buffer.readLong();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		Group group = The5zigMod.getGroupChatManager().getGroup(groupId);
		if (group == null)
			return;
		The5zigMod.getConversationManager().handleGroupBroadcast(group, message, time);
	}
}
