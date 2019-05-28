package eu.the5zig.mod.chat.entity;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.packets.PacketCapeSettings;
import eu.the5zig.mod.chat.network.packets.PacketProfile;
import eu.the5zig.util.minecraft.ChatColor;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class Profile {

	private final int id;
	private final Rank rank;
	private final long firstTime;
	private String profileMessage;
	private Friend.OnlineStatus onlineStatus;
	private boolean showServer;
	private boolean showMessageRead;
	private boolean showFriendRequests;
	private boolean showCape;
	private boolean showCountry;
	private ChatColor displayColor;

	public Profile(int id, Rank rank, long firstTime, String profileMessage, Friend.OnlineStatus onlineStatus, boolean showServer, boolean showMessageRead, boolean showFriendRequests,
			boolean showCape, boolean showCountry, ChatColor displayColor) {
		this.id = id;
		this.rank = rank;
		this.firstTime = firstTime;
		this.profileMessage = profileMessage;
		this.onlineStatus = onlineStatus;
		this.showServer = showServer;
		this.showMessageRead = showMessageRead;
		this.showFriendRequests = showFriendRequests;
		this.showCape = showCape;
		this.showCountry = showCountry;
		this.displayColor = displayColor;
	}

	public int getId() {
		return id;
	}

	public Rank getRank() {
		return rank;
	}

	public long getFirstTime() {
		return firstTime;
	}

	public String getProfileMessage() {
		return profileMessage;
	}

	public void setProfileMessage(String profileMessage) {
		this.profileMessage = profileMessage;
		The5zigMod.getNetworkManager().sendPacket(new PacketProfile(profileMessage));
	}

	public Friend.OnlineStatus getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(Friend.OnlineStatus onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public boolean isShowServer() {
		return showServer;
	}

	public void setShowServer(boolean showServer) {
		this.showServer = showServer;
		The5zigMod.getNetworkManager().sendPacket(new PacketProfile(PacketProfile.ProfileType.SHOW_SERVER, showServer));
	}

	public boolean isShowMessageRead() {
		return showMessageRead;
	}

	public void setShowMessageRead(boolean showMessageRead) {
		this.showMessageRead = showMessageRead;
		The5zigMod.getNetworkManager().sendPacket(new PacketProfile(PacketProfile.ProfileType.SHOW_MESSAGE_READ, showMessageRead));
	}

	public boolean isShowFriendRequests() {
		return showFriendRequests;
	}

	public void setShowFriendRequests(boolean showFriendRequests) {
		this.showFriendRequests = showFriendRequests;
		The5zigMod.getNetworkManager().sendPacket(new PacketProfile(PacketProfile.ProfileType.SHOW_FRIEND_REQUESTS, showFriendRequests));
	}

	public boolean isShowCountry() {
		return showCountry;
	}

	public void setShowCountry(boolean showCountry) {
		this.showCountry = showCountry;
		The5zigMod.getNetworkManager().sendPacket(new PacketProfile(PacketProfile.ProfileType.SHOW_COUNTRY, showCountry));
	}

	public boolean isCapeEnabled() {
		return getRank() != Rank.NONE && showCape;
	}

	public void setCapeEnabled(boolean capeEnabled) {
		this.showCape = capeEnabled;
		The5zigMod.getNetworkManager().sendPacket(new PacketCapeSettings(PacketCapeSettings.Action.SETTINGS, capeEnabled));
	}

	public ChatColor getDisplayColor() {
		return displayColor;
	}

	public void setDisplayColor(ChatColor color) {
		this.displayColor = color;
		The5zigMod.getNetworkManager().sendPacket(new PacketProfile(PacketProfile.ProfileType.DISPLAY_COLOR, displayColor));
	}

}
