package eu.the5zig.mod.chat.network.util;

import eu.the5zig.mod.chat.network.packets.Packet;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketHandleTask implements Runnable {

	private Packet packet;

	public PacketHandleTask(Packet packet) {
		this.packet = packet;
	}

	@Override
	public void run() {
		packet.handle();
	}
}
