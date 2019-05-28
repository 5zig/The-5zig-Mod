package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketFileTransferStart implements Packet {

	private int fileId;
	private int parts;
	private int chunkSize;

	public PacketFileTransferStart(int fileId, int parts, int chunkSize) {
		this.fileId = fileId;
		this.parts = parts;
		this.chunkSize = chunkSize;
	}

	public PacketFileTransferStart() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.fileId = PacketBuffer.readVarIntFromBuffer(buffer);
		this.parts = PacketBuffer.readVarIntFromBuffer(buffer);
		this.chunkSize = PacketBuffer.readVarIntFromBuffer(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, fileId);
		PacketBuffer.writeVarIntToBuffer(buffer, parts);
		PacketBuffer.writeVarIntToBuffer(buffer, chunkSize);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getConversationManager().handleFileStart(fileId, parts, chunkSize);
	}

	public enum Type {
		IMAGE, AUDIO
	}

}
