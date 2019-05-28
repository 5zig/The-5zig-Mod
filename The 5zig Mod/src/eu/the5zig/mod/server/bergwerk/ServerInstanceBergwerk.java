package eu.the5zig.mod.server.bergwerk;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstanceBergwerk extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new BergwerkListener());
		getGameListener().registerListener(new BergwerkFlashListener());
		getGameListener().registerListener(new BergwerkDuelListener());
	}

	@Override
	public String getName() {
		return "Bergwerklabs.de";
	}

	@Override
	public String getConfigName() {
		return "bergwerk";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("bergwerklabs.de");
	}
}
