package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Friend;
import eu.the5zig.mod.chat.entity.Profile;
import eu.the5zig.mod.chat.entity.Rank;
import eu.the5zig.util.minecraft.ChatColor;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketProfile implements Packet {

	private int id;
	private Rank rank;
	private long firstConnectTime;
	private String profileMessage;
	private Friend.OnlineStatus onlineStatus;
	private boolean showServer;
	private boolean showMessageRead;
	private boolean showFriendRequests;
	private boolean showCape;
	private boolean showCountry;
	private ChatColor displayColor;

	private ProfileType profileType;
	private boolean show;

	public PacketProfile(String profileMessage) {
		this.profileType = ProfileType.PROFILE_MESSAGE;
		this.profileMessage = profileMessage;
	}

	public PacketProfile(Friend.OnlineStatus onlineStatus) {
		this.profileType = ProfileType.ONLINE_STATUS;
		this.onlineStatus = onlineStatus;
	}

	public PacketProfile(ProfileType profileType, boolean show) {
		this.profileType = profileType;
		this.show = show;
	}

	public PacketProfile(ProfileType profileType, ChatColor displayColor) {
		this.profileType = profileType;
		this.displayColor = displayColor;
	}

	public PacketProfile() {
	}

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.id = PacketBuffer.readVarIntFromBuffer(buffer);
		this.rank = PacketBuffer.readRank(buffer);
		this.firstConnectTime = buffer.readLong();
		this.profileMessage = PacketBuffer.readString(buffer);
		int ordinal = PacketBuffer.readVarIntFromBuffer(buffer);
		if (ordinal < 0 || ordinal >= Friend.OnlineStatus.values().length)
			throw new IllegalArgumentException("Received Integer is out of enum range");
		this.onlineStatus = Friend.OnlineStatus.values()[ordinal];
		this.showServer = buffer.readBoolean();
		this.showMessageRead = buffer.readBoolean();
		this.showFriendRequests = buffer.readBoolean();
		this.showCape = buffer.readBoolean();
		this.showCountry = buffer.readBoolean();
		this.displayColor = ChatColor.getByChar(buffer.readChar());
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
		PacketBuffer.writeVarIntToBuffer(buffer, profileType.ordinal());
		if (profileType == ProfileType.PROFILE_MESSAGE) {
			PacketBuffer.writeString(buffer, profileMessage);
		} else if (profileType == ProfileType.ONLINE_STATUS) {
			PacketBuffer.writeVarIntToBuffer(buffer, onlineStatus.ordinal());
		} else if (profileType == ProfileType.DISPLAY_COLOR) {
			buffer.writeChar(displayColor.getCode());
		} else {
			buffer.writeBoolean(show);
		}
	}

	@Override
	public void handle() {
		The5zigMod.getDataManager().setProfile(new Profile(id, rank, firstConnectTime, profileMessage, onlineStatus, showServer, showMessageRead, showFriendRequests, showCape, showCountry,
				displayColor));
	}

	public enum ProfileType {

		PROFILE_MESSAGE, ONLINE_STATUS, SHOW_SERVER, SHOW_MESSAGE_READ, SHOW_FRIEND_REQUESTS, SHOW_COUNTRY, DISPLAY_COLOR

	}
}
