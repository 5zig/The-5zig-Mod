package eu.the5zig.mod.server.hypixel;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstanceHypixel extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new HypixelListener());
		getGameListener().registerListener(new HypixelQuakeListener());
		getGameListener().registerListener(new HypixelBlitzListener());
		getGameListener().registerListener(new HypixelPaintballListener());
	}

	@Override
	public String getName() {
		return "Hypixel.net";
	}

	@Override
	public String getConfigName() {
		return "hypixel";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("hypixel.net");
	}
}
