package eu.the5zig.mod.chat.network;

import eu.the5zig.mod.chat.network.packets.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.zip.Deflater;

public class NetworkCompressionEncoder extends MessageToByteEncoder<ByteBuf> {

	private final byte[] buffer = new byte[8192];
	private final Deflater deflater;
	private int threshold;

	public NetworkCompressionEncoder(int threshold) {
		this.threshold = threshold;
		this.deflater = new Deflater();
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		this.deflater.end();
	}

	protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
		int length = in.readableBytes();

		if (length < this.threshold) {
			PacketBuffer.writeVarIntToBuffer(out, 0);
			out.writeBytes(in);
		} else {
			byte[] uncompressedData = new byte[length];
			in.readBytes(uncompressedData);
			PacketBuffer.writeVarIntToBuffer(out, uncompressedData.length);
			this.deflater.setInput(uncompressedData, 0, length);
			this.deflater.finish();

			while (!this.deflater.finished()) {
				int compressedLength = this.deflater.deflate(this.buffer);
				out.writeBytes(this.buffer, 0, compressedLength);
			}

			this.deflater.reset();
		}
	}

	public void setCompressionTreshold(int treshold) {
		this.threshold = treshold;
	}

}