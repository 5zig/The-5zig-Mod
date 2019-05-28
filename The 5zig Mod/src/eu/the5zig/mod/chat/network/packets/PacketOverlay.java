package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketOverlay implements Packet {

	private Type type;
	private String message;
	private Object[] format;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		int ordinal = PacketBuffer.readVarIntFromBuffer(buffer);
		if (ordinal < 0 || ordinal >= Type.values().length)
			throw new IllegalArgumentException("Received Integer is out of enum range");
		this.type = Type.values()[ordinal];
		this.message = PacketBuffer.readString(buffer);
		if (type != Type.NONE) {
			int formatLength = PacketBuffer.readVarIntFromBuffer(buffer);
			this.format = new String[formatLength];
			for (int i = 0; i < formatLength; i++) {
				this.format[i] = PacketBuffer.readString(buffer);
			}
		} else {
			format = new String[0];
		}
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		switch (type) {
			case INFO:
				The5zigMod.getOverlayMessage().displayMessageAndSplit(ChatColor.YELLOW + I18n.translate(message, format));
				break;
			case SUCCESS:
				The5zigMod.getOverlayMessage().displayMessageAndSplit(ChatColor.GREEN + I18n.translate(message, format));
				break;
			case ERROR:
				The5zigMod.getOverlayMessage().displayMessageAndSplit(ChatColor.RED + I18n.translate(message, format));
				break;
			default:
				The5zigMod.getOverlayMessage().displayMessageAndSplit(ChatColor.YELLOW + message);
				break;
		}
	}

	public enum Type {
		NONE, INFO, SUCCESS, ERROR // NONE won't be translated, INFO will be translated in yellow, SUCCESS will be translated in green, ERROR will be translated in red.
	}

}
