package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

public class PacketFileTransferId implements Packet {

	private UUID uuid;
	private int conversationId;
	private int fileId;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.uuid = PacketBuffer.readUUID(buffer);
		this.conversationId = PacketBuffer.readVarIntFromBuffer(buffer);
		this.fileId = PacketBuffer.readVarIntFromBuffer(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getConversationManager().handleFileId(uuid, conversationId, fileId);
	}
}
