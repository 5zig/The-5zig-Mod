package eu.the5zig.mod.chat;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.NetworkManager;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TickEvent;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class NetworkTickListener {

	@EventHandler
	public void onTick(TickEvent event) {
		NetworkManager networkManager = The5zigMod.getNetworkManager();
		networkManager.tick();

		if (networkManager.isConnected()) {
			if (networkManager.getHeartbeatManager() != null) {
				networkManager.getHeartbeatManager().onTick();
			}
			The5zigMod.getDataManager().getNetworkStats().tick();
		}
	}
}
