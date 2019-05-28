package eu.the5zig.mod.chat.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import javax.crypto.Cipher;
import java.util.List;

public class NettyEncryptingDecoder extends MessageToMessageDecoder<ByteBuf> {
	private final NettyEncryptionTranslator decryptionCodec;

	public NettyEncryptingDecoder(Cipher cipher) {
		this.decryptionCodec = new NettyEncryptionTranslator(cipher);
	}

	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
		out.add(this.decryptionCodec.decipher(channelHandlerContext, in));
	}
}
