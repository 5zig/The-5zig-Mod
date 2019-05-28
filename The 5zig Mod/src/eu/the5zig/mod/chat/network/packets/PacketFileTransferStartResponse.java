package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.util.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketFileTransferStartResponse implements Packet {

	private int fileId;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.fileId = PacketBuffer.readVarIntFromBuffer(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		PacketUtil.ensureMainThread(this);

		The5zigMod.getConversationManager().handleStartResponse(fileId);
	}
}
