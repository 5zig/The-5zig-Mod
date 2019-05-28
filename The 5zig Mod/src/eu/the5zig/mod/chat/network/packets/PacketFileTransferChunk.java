package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.filetransfer.FileUploadTask;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketFileTransferChunk implements Packet {

	private int transferId;
	private int partId;
	private byte[] data;
	private int length;

	public PacketFileTransferChunk(int transferId, int partId, FileUploadTask.Chunk chunk) {
		this.transferId = transferId;
		this.partId = partId;
		this.data = chunk.getData();
		this.length = chunk.getLength();
	}

	public PacketFileTransferChunk() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.transferId = PacketBuffer.readVarIntFromBuffer(buffer);
		this.partId = PacketBuffer.readVarIntFromBuffer(buffer);
		this.data = new byte[buffer.readableBytes()];
		buffer.readBytes(data);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, transferId);
		PacketBuffer.writeVarIntToBuffer(buffer, partId);
		buffer.writeBytes(data, 0, length);
	}

	@Override
	public void handle() {
		The5zigMod.getConversationManager().handleFileChunk(transferId, partId, data);
	}
}
