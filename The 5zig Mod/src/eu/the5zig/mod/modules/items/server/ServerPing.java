package eu.the5zig.mod.modules.items.server;

import com.mojang.authlib.GameProfile;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;
import eu.the5zig.mod.util.NetworkPlayerInfo;
import eu.the5zig.util.minecraft.ChatColor;

public class ServerPing extends StringItem {

	private long lastPinged;
	private int ping;

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 27;
		}
		if (The5zigMod.getDataManager().getServer() == null) {
			return null;
		}
		int ping = checkPing();
		return ping != 0 ? ping : I18n.translate("ingame.pinging");
	}

	private int checkPing() {
		long l = System.currentTimeMillis();
		if (l - lastPinged < 1000) {
			return ping;
		}
		lastPinged = l;

		GameProfile gameProfile = The5zigMod.getDataManager().getGameProfile();
		for (NetworkPlayerInfo networkPlayerInfo : The5zigMod.getVars().getServerPlayers()) {
			if (gameProfile.equals(networkPlayerInfo.getGameProfile()) || gameProfile.getName().equals(ChatColor.stripColor(networkPlayerInfo.getDisplayName()))
					|| (networkPlayerInfo.getGameProfile() != null && gameProfile.getName().equals(networkPlayerInfo.getGameProfile().getName()))) {
				if (networkPlayerInfo.getPing() <= 0) {
					ping = 0;
				} else {
					ping = networkPlayerInfo.getPing();
				}
				break;
			}
		}

		return ping;
	}

	@Override
	public String getTranslation() {
		return "ingame.ping";
	}
}
