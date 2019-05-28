package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketFileTransferResponse implements Packet {

	private int fileId;
	private boolean accepted;

	public PacketFileTransferResponse(int fileId, boolean accepted) {
		this.fileId = fileId;
		this.accepted = accepted;
	}

	public PacketFileTransferResponse() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.fileId = PacketBuffer.readVarIntFromBuffer(buffer);
		this.accepted = buffer.readBoolean();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, fileId);
		buffer.writeBoolean(accepted);
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getConversationManager().handleFileResponse(fileId, accepted);
	}
}
