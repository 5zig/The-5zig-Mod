package eu.the5zig.mod.server.gomme;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstanceGommeHD extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new GommeHDListener());
		getGameListener().registerListener(new GommeHDPvPListener());
		getGameListener().registerListener(new GommeHDPvPFFAListener());
		getGameListener().registerListener(new GommeHDSurvivalGamesListener());
		getGameListener().registerListener(new GommeHDEnderGamesListener());
		getGameListener().registerListener(new GommeHDBedWarsListener());
		getGameListener().registerListener(new GommeHDSkyWarsListener());
		getGameListener().registerListener(new GommeHDRageModeListener());
	}

	@Override
	public String getName() {
		return "GommeHD.net";
	}

	@Override
	public String getConfigName() {
		return "gommehd";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("gommehd.net") || host.toLowerCase(Locale.ROOT).endsWith("gommehd.de") || host.toLowerCase(Locale.ROOT).endsWith("gommehd.tk");
	}

}
