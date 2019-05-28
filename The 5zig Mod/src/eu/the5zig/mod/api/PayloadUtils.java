package eu.the5zig.mod.api;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.packets.PacketBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PayloadUtils {

	public static final String API_CHANNEL = "5zig";
	public static final String API_CHANNEL_REGISTER = "5zig:reg";

	public static final String SETTING_CHANNEL = "5zig:set";

	public static void sendPayload(String message) {
		sendPayload(API_CHANNEL, message);
	}

	public static void sendPayload(ByteBuf buf) {
		sendPayload(API_CHANNEL, buf);
	}

	public static void sendPayload(String channel, String message) {
		sendPayload(channel, Unpooled.buffer().writeBytes(message.getBytes()));
	}

	public static void sendPayload(String channel, ByteBuf buf) {
		if (The5zigMod.getVars().hasNetworkManager()) {
			The5zigMod.getVars().sendCustomPayload(channel, buf);
		}
	}

	public static ByteBuf writeString(ByteBuf byteBuf, String string) {
		byte[] bytes = string.getBytes(org.apache.commons.codec.Charsets.UTF_8);
		byteBuf.writeBytes(bytes);
		return byteBuf;
	}

	public static String readString(ByteBuf byteBuf, int maxLength) {
		int length = PacketBuffer.readVarIntFromBuffer(byteBuf);
		if (length > maxLength * 4) {
			throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + length + " > " + maxLength * 4 + ")");
		} else if (length < 0) {
			throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
		} else {
			String var2 = byteBuf.toString(byteBuf.readerIndex(), length, org.apache.commons.codec.Charsets.UTF_8);
			byteBuf.readerIndex(byteBuf.readerIndex() + length);
			if (var2.length() > maxLength) {
				throw new DecoderException("The received string length is longer than maximum allowed (" + length + " > " + maxLength + ")");
			} else {
				return var2;
			}
		}
	}

}
