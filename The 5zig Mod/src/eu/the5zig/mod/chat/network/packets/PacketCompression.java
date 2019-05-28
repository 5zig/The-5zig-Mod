package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PacketCompression implements Packet {

	private int threshold;

	public PacketCompression(int threshold) {
		this.threshold = threshold;
	}

	public PacketCompression() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.threshold = PacketBuffer.readVarIntFromBuffer(buffer);
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		The5zigMod.getNetworkManager().setThreshold(threshold);
		The5zigMod.logger.info("Enabled Compression (threshold=" + threshold + ")");
	}

	public int getThreshold() {
		return threshold;
	}
}
