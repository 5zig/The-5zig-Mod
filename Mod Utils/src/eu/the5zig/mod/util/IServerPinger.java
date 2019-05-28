package eu.the5zig.mod.util;

public interface IServerPinger {

	IServerData createServerData(String serverIP);

	void ping(IServerData serverData);

	void pingPendingNetworks();

	void clearPendingNetworks();

}
