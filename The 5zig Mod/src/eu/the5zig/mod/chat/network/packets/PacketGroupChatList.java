package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Group;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketGroupChatList implements Packet {

	private List<Group> groups;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		int size = PacketBuffer.readVarIntFromBuffer(buffer);
		groups = new ArrayList<Group>(size);
		for (int i = 0; i < size; i++) {
			groups.add(PacketBuffer.readGroup(buffer));
		}
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);
		The5zigMod.getGroupChatManager().setGroups(groups);
	}
}
