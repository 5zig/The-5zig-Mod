package eu.the5zig.mod.manager;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.Server;
import eu.the5zig.util.Utils;

public class AutoReconnectManager {

	private long countdownStartTime;
	private Object parentScreen;
	private Object lastServerData;

	public void setServerData(Object serverData) {
		this.lastServerData = serverData;
	}

	public void startCountdown(Object parentScreen) {
		Server server = The5zigMod.getDataManager().getServer();
		The5zigMod.getDataManager().resetServer();
		if (lastServerData == null) {
			return;
		}
		System.out.println(The5zigMod.getConfig().getInt("autoServerReconnect"));
		if (The5zigMod.getConfig().getInt("autoServerReconnect") < 5 || (server != null && !server.isAutoReconnecting())) {
			countdownStartTime = 0;
			return;
		}
		countdownStartTime = System.currentTimeMillis();
		this.parentScreen = parentScreen;
	}

	public void checkCountdown(int guiWidth, int guiHeight) {
		if (lastServerData == null || countdownStartTime == 0) {
			return;
		}
		if (System.currentTimeMillis() - countdownStartTime > The5zigMod.getConfig().getInt("autoServerReconnect") * 1000) {
			The5zigMod.getVars().joinServer(parentScreen, lastServerData);
			countdownStartTime = 0;
		} else {
			The5zigMod.getVars().drawCenteredString(
					I18n.translate("server.reconnecting", Utils.getShortenedDouble((The5zigMod.getConfig().getInt("autoServerReconnect")
							* 1000 - (System.currentTimeMillis() - countdownStartTime)) / 1000.0, 1)), guiWidth / 2,
					guiHeight - 12);
		}
	}

}
