package eu.the5zig.mod.chat.network;

import eu.the5zig.mod.chat.network.packets.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class NetworkPrepender extends ByteToMessageDecoder {

	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> objects) {
		buffer.markReaderIndex();
		byte[] var4 = new byte[3];

		for (int var5 = 0; var5 < var4.length; ++var5) {
			if (!buffer.isReadable()) {
				buffer.resetReaderIndex();
				return;
			}

			var4[var5] = buffer.readByte();

			if (var4[var5] >= 0) {
				ByteBuf var6 = Unpooled.wrappedBuffer(var4);

				try {
					int var7 = PacketBuffer.readVarIntFromBuffer(var6);

					if (buffer.readableBytes() < var7) {
						buffer.resetReaderIndex();
						return;
					}

					objects.add(buffer.readBytes(var7));
				} finally {
					var6.release();
				}

				return;
			}
		}

		throw new CorruptedFrameException("length wider than 21-bit");
	}

}
