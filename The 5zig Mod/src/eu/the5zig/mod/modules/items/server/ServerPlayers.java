package eu.the5zig.mod.modules.items.server;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;

public class ServerPlayers extends StringItem {

	private long lastChecked;
	private int serverPlayers;

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 64;
		}
		return The5zigMod.getDataManager().getServer() == null ? null : getServerPlayers();
	}

	private int getServerPlayers() {
		if (System.currentTimeMillis() - lastChecked < 500) {
			return serverPlayers;
		}
		lastChecked = System.currentTimeMillis();
		return serverPlayers = The5zigMod.getVars().getServerPlayers().size();
	}

	@Override
	public String getTranslation() {
		return "ingame.players";
	}
}
