package eu.the5zig.mod.chat.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;

public class NettyEncryptingEncoder extends MessageToByteEncoder<ByteBuf> {
	private final NettyEncryptionTranslator encryptionCodec;

	public NettyEncryptingEncoder(Cipher cipher) {
		this.encryptionCodec = new NettyEncryptionTranslator(cipher);
	}

	protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf in, ByteBuf out) throws Exception {
		this.encryptionCodec.cipher(in, out);
	}
}
