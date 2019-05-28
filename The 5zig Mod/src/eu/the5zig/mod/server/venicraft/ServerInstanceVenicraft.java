package eu.the5zig.mod.server.venicraft;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstanceVenicraft extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new VenicraftListener());
		getGameListener().registerListener(new VenicraftMineathlonListener());
	}

	@Override
	public String getName() {
		return "Venicraft.at";
	}

	@Override
	public String getConfigName() {
		return "venicraft";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("venicraft.at");
	}

}
