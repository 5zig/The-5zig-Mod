package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketFileTransferAbort implements Packet {

	private int fileId;

	public PacketFileTransferAbort() {
	}

	public PacketFileTransferAbort(int fileId) {
		this.fileId = fileId;
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.fileId = PacketBuffer.readVarIntFromBuffer(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, fileId);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);
		The5zigMod.getConversationManager().handleFileAbort(fileId);
	}
}
