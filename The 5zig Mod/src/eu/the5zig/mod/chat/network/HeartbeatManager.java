package eu.the5zig.mod.chat.network;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.packets.PacketHeartbeat;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class HeartbeatManager {

	private long lastHeartbeat;

	public HeartbeatManager() {
		lastHeartbeat = System.currentTimeMillis();
	}

	public void onTick() {
		if (System.currentTimeMillis() - lastHeartbeat > 1000 * 20) {
			The5zigMod.getNetworkManager().disconnect(I18n.translate("connection.timed_out"));
		}
	}

	public void onHeartbeatReceive(int id) {
		lastHeartbeat = System.currentTimeMillis();
		The5zigMod.getNetworkManager().sendPacket(new PacketHeartbeat(id));
	}

}
