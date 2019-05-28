package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Message;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketGroupChatMessageStatusSent implements Packet {

	private int groupId;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.groupId = PacketBuffer.readVarIntFromBuffer(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);
		The5zigMod.getConversationManager().setConversationStatus(The5zigMod.getConversationManager().getConversation(The5zigMod.getGroupChatManager().getGroup(groupId)),
				Message.MessageStatus.SENT);
	}
}
