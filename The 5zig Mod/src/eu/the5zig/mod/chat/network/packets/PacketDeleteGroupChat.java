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
public class PacketDeleteGroupChat implements Packet {

	private int groupId;

	public PacketDeleteGroupChat(int groupId) {
		this.groupId = groupId;
	}

	public PacketDeleteGroupChat() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.groupId = PacketBuffer.readVarIntFromBuffer(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, groupId);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);
		Group group = The5zigMod.getGroupChatManager().getGroup(groupId);
		if (group == null)
			return;
		The5zigMod.getGroupChatManager().removeGroup(group);
	}
}
