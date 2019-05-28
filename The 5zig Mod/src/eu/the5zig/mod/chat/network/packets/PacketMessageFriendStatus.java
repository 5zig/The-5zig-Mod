package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Friend;
import eu.the5zig.mod.chat.entity.Message;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketMessageFriendStatus implements Packet {

	private UUID friend;
	private Message.MessageStatus messageStatus;

	public PacketMessageFriendStatus(UUID friend, Message.MessageStatus messageStatus) {
		this.friend = friend;
		this.messageStatus = messageStatus;
	}

	public PacketMessageFriendStatus() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.friend = PacketBuffer.readUUID(buffer);
		this.messageStatus = Message.MessageStatus.values()[PacketBuffer.readVarIntFromBuffer(buffer)];
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUUID(buffer, friend);
		PacketBuffer.writeVarIntToBuffer(buffer, messageStatus.ordinal());
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);
		Friend f = The5zigMod.getFriendManager().getFriend(friend);
		if (f == null || !The5zigMod.getConversationManager().conversationExists(f))
			return;
		The5zigMod.getConversationManager().setConversationStatus(The5zigMod.getConversationManager().getConversation(f), messageStatus);
	}
}
