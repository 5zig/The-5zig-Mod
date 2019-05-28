package eu.the5zig.mod.config;

import eu.the5zig.mod.server.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class LastServer {

	private List<Server> lastServers;
	private final int MAX_SERVER_COUNT = 5;

	public Server getLastServer() {
		return lastServers == null || lastServers.isEmpty() ? null : lastServers.get(0);
	}

	public void setLastServer(Server lastServer) {
		if (this.lastServers == null)
			this.lastServers = new ArrayList<Server>();
		this.lastServers.remove(lastServer);
		this.lastServers.add(0, lastServer);
		while (this.lastServers.size() > MAX_SERVER_COUNT)
			this.lastServers.remove(MAX_SERVER_COUNT);
	}

}
