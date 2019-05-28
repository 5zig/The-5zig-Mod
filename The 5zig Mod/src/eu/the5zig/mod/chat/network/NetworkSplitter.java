package eu.the5zig.mod.chat.network;

import eu.the5zig.mod.chat.network.packets.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class NetworkSplitter extends MessageToByteEncoder<ByteBuf> {

	protected void encode(ChannelHandlerContext ctx, ByteBuf out, ByteBuf byteBuf) {
		int var4 = out.readableBytes();
		int var5 = PacketBuffer.getVarIntSize(var4);

		if (var5 > 3) {
			throw new IllegalArgumentException("unable to fit " + var4 + " into " + 3);
		} else {
			byteBuf.ensureWritable(var5 + var4);
			PacketBuffer.writeVarIntToBuffer(byteBuf, var4);
			byteBuf.writeBytes(out, out.readerIndex(), var4);
		}
	}

}
