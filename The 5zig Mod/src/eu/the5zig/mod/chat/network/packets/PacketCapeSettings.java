package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketCapeSettings implements Packet {

	private Action action;
	private byte[] image;
	private boolean enabled;
	private Cape cape;

	public PacketCapeSettings(byte[] image) {
		this.action = Action.UPLOAD_CUSTOM;
		this.image = image;
	}

	public PacketCapeSettings(Action action, boolean enabled) {
		this.action = action;
		this.enabled = enabled;
	}

	public PacketCapeSettings(Action action, Cape cape) {
		this.action = action;
		this.cape = cape;
	}

	public PacketCapeSettings() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		int ordinal = PacketBuffer.readVarIntFromBuffer(buffer);
		if (ordinal < 0 || ordinal >= Action.values().length)
			throw new IllegalArgumentException("Received Integer is out of enum range");
		this.action = Action.values()[ordinal];
		if (action == Action.SETTINGS)
			this.enabled = buffer.readBoolean();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, action.ordinal());
		if (action == Action.UPLOAD_CUSTOM) {
			buffer.writeBytes(image);
		}
		if (action == Action.UPLOAD_DEFAULT)
			PacketBuffer.writeVarIntToBuffer(buffer, cape.ordinal());
		if (action == Action.SETTINGS)
			buffer.writeBoolean(enabled);
	}

	@Override
	public void handle() {
		if (action == Action.UPDATE) {
			The5zigMod.getVars().getResourceManager().updateOwnPlayerTextures();
		}
	}

	public enum Action {
		SETTINGS, UPLOAD_CUSTOM, UPLOAD_DEFAULT, UPDATE
	}

	public enum Cape {
		GREEN, RED, BLUE, YELLOW, XMAS, NEW_YEAR, SNOWMAN, STAR, MINECON_2011, MINECON_2012, MINECON_2013, MINECON_2015, SCROLLS, COBALT, MOJANG, MOJANG_2016, BACON, PRISMARINE,
		MRMESSIAH, MAPMAKER, BUG_TRACKER, CROWDIN, ANIMATED_FIRE, MINECON_2016
	}

}
