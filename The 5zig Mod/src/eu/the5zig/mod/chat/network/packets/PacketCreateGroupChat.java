package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Group;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketCreateGroupChat implements Packet {

	private List<UUID> players;
	private String name;

	private Group group;

	public PacketCreateGroupChat(List<UUID> players, String name) {
		this.players = players;
		this.name = name;
	}

	public PacketCreateGroupChat() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.group = PacketBuffer.readGroup(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		buffer.writeInt(players.size());
		for (UUID player : players) {
			PacketBuffer.writeUUID(buffer, player);
		}
		PacketBuffer.writeString(buffer, name);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getGroupChatManager().addGroup(group);
	}
}