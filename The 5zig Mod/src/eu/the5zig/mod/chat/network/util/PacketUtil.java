package eu.the5zig.mod.chat.network.util;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.network.packets.Packet;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketUtil {

	public static void ensureMainThread(Packet packet) {
		if (!The5zigMod.getVars().isMainThread()) {
			The5zigMod.getScheduler().postToMainThread(new PacketHandleTask(packet));
			throw CancelPacketHandleException.INSTANCE;
		}
	}

}
