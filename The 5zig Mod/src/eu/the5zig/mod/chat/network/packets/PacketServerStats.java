package eu.the5zig.mod.chat.network.packets;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.NetworkStats;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PacketServerStats implements Packet {

	private int connectedClients;
	private int maxClients;
	private long startTime;
	private int ping;
	private int lag;

	@Override
	public void read(ByteBuf buffer) throws IOException {
		this.connectedClients = buffer.readInt();
		this.maxClients = buffer.readInt();
		this.startTime = buffer.readLong();
		this.ping = buffer.readInt();
		this.lag = buffer.readInt();
	}

	@Override
	public void write(ByteBuf buffer) throws IOException {
	}

	@Override
	public void handle() {
		NetworkStats networkStats = The5zigMod.getDataManager().getNetworkStats();
		networkStats.setConnectedClients(connectedClients);
		networkStats.setMaxClients(maxClients);
		networkStats.setStartTime(startTime);
		networkStats.setPing(ping);
		networkStats.setLag(lag);
	}
}
