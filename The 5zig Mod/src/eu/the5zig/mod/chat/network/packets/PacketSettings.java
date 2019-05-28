package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketSettings implements Packet {

	private SettingType settingType;
	private String status;
	private boolean enabled;

	public PacketSettings(SettingType settingType, String status) {
		this.settingType = settingType;
		this.status = status;
	}

	public PacketSettings(boolean enabled, SettingType settingType) {
		this.enabled = enabled;
		this.settingType = settingType;
	}

	public PacketSettings() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		int i = buffer.readInt();
		if (i < 0 || i >= SettingType.values().length)
			throw new IllegalArgumentException("Received Integer is out of enum range");
		this.settingType = SettingType.values()[i];
		if (settingType == SettingType.PROFILE_MESSAGE) {
			this.status = PacketBuffer.readString(buffer);
		}
		if (settingType == SettingType.SHOW_CURRENT_SERVER || settingType == SettingType.SHOW_LAST_SEEN_TIME) {
			this.enabled = buffer.readBoolean();
		}
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		buffer.writeInt(settingType.ordinal());
		if (settingType == SettingType.PROFILE_MESSAGE) {
			PacketBuffer.writeString(buffer, status);
		}
		if (settingType == SettingType.SHOW_CURRENT_SERVER || settingType == SettingType.SHOW_LAST_SEEN_TIME) {
			buffer.writeBoolean(enabled);
		}
	}

	@Override
	public void handle() {
		if (settingType == SettingType.PROFILE_MESSAGE)
			The5zigMod.getDataManager().getProfile().setProfileMessage(status);
	}

	public enum SettingType {

		PROFILE_MESSAGE, SHOW_LAST_SEEN_TIME, SHOW_CURRENT_SERVER

	}
}
