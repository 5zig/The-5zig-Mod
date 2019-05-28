package eu.the5zig.mod.modules.items.server;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;
import eu.the5zig.mod.server.Server;

public class ServerIP extends StringItem {
	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "hypixel.net";
		}
		Server server = The5zigMod.getDataManager().getServer();
		if (server == null) {
			return null;
		}
		String ip = server.getHost();
		if (server.getPort() != 25565)
			ip += ":" + server.getPort();

		return The5zigMod.getVars().shortenToWidth(ip, 150);
	}

	@Override
	public String getTranslation() {
		return "ingame.ip";
	}
}
