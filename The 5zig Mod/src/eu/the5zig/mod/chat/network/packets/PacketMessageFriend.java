package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketMessageFriend implements Packet {

	private UUID friend;
	private String username;
	private String message;
	private long time;

	public PacketMessageFriend(UUID friend, String message, long time) {
		this.friend = friend;
		this.message = message;
		this.time = time;
	}

	public PacketMessageFriend() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.friend = PacketBuffer.readUUID(buffer);
		this.username = PacketBuffer.readString(buffer);
		this.message = PacketBuffer.readString(buffer);
		this.time = buffer.readLong();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUUID(buffer, friend);
		PacketBuffer.writeString(buffer, message);
		buffer.writeLong(time);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		if (The5zigMod.getConfig().getBool("playMessageSounds"))
			The5zigMod.getVars().playSound("the5zigmod", "chat.message.receive", 1);

		The5zigMod.getConversationManager().handleFriendMessageReceive(friend, username, message, time);
	}
}
