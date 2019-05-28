package eu.the5zig.mod.server.mineplex;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstanceMineplex extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new MineplexListener());
	}

	@Override
	public String getName() {
		return "Mineplex.com";
	}

	@Override
	public String getConfigName() {
		return "mineplex";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("mineplex.com") || host.toLowerCase(Locale.ROOT).endsWith("mineplex.eu") || host.toLowerCase(Locale.ROOT).endsWith("mineplex.us");
	}
}
