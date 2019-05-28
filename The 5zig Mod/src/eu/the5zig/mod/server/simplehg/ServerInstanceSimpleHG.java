package eu.the5zig.mod.server.simplehg;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstanceSimpleHG extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new SimpleHGListener());
		getGameListener().registerListener(new SimpleHGGameListener());
	}

	@Override
	public String getName() {
		return "SimpleHG";
	}

	@Override
	public String getConfigName() {
		return "simplehg";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("simplehg.com") || host.toLowerCase(Locale.ROOT).endsWith("simplehg.net") || host.toLowerCase(Locale.ROOT).endsWith("simplehg.eu") ||
				host.toLowerCase(Locale.ROOT).endsWith("simplehg.de") || host.toLowerCase(Locale.ROOT).endsWith("simplegalaxy.net");
	}
}
