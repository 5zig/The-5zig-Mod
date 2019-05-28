package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.UUID;

public class PacketFileTransferRequest implements Packet {

	private UUID uuid;
	private int fileId;
	private PacketFileTransferStart.Type type;
	private long length;

	public PacketFileTransferRequest(UUID uuid, int fileId, PacketFileTransferStart.Type type, long length) {
		this.uuid = uuid;
		this.fileId = fileId;
		this.type = type;
		this.length = length;
	}

	public PacketFileTransferRequest() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.uuid = PacketBuffer.readUUID(buffer);
		this.fileId = PacketBuffer.readVarIntFromBuffer(buffer);
		this.type = PacketBuffer.readEnum(buffer, PacketFileTransferStart.Type.class);
		this.length = buffer.readLong();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeUUID(buffer, uuid);
		PacketBuffer.writeVarIntToBuffer(buffer, fileId);
		PacketBuffer.writeEnum(buffer, type);
		buffer.writeLong(length);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		if (The5zigMod.getConfig().getBool("playMessageSounds"))
			The5zigMod.getVars().playSound("the5zigmod", "chat.message.receive", 1);

		The5zigMod.getConversationManager().handleFileRequest(uuid, fileId, type, length);
	}

}
