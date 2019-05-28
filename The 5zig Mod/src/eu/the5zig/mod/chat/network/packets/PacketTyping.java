package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.manager.ChatTypingManager;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketTyping implements Packet {

	private UUID friend;
	private boolean typing;

	public PacketTyping(UUID friend, boolean typing) {
		this.friend = friend;
		this.typing = typing;
	}

	public PacketTyping() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.friend = PacketBuffer.readUUID(buffer);
		this.typing = buffer.readBoolean();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUUID(buffer, friend);
		buffer.writeBoolean(typing);
	}

	@Override
	public void handle() {
		ChatTypingManager manager = The5zigMod.getDataManager().getChatTypingManager();
		if (typing) {
			manager.addToTyping(friend);
		} else {
			manager.removeFromTyping(friend);
		}
	}
}
