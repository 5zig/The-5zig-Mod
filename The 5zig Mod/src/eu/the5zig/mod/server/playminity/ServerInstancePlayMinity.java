package eu.the5zig.mod.server.playminity;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstancePlayMinity extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new PlayMinityListener());
		getGameListener().registerListener(new PlayMinityJumpLeagueListener());
	}

	@Override
	public String getName() {
		return "PlayMinity.com";
	}

	@Override
	public String getConfigName() {
		return "playminity";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("playminity.com");
	}
}
