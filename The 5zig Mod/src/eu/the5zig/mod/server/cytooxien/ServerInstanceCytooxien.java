package eu.the5zig.mod.server.cytooxien;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstanceCytooxien extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new CytooxienListener());
		getGameListener().registerListener(new CytooxienMarioPartyListener());
		getGameListener().registerListener(new CytooxienBedwarsListener());
	}

	@Override
	public String getName() {
		return "Cytooxien";
	}

	@Override
	public String getConfigName() {
		return "cytooxien";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("cytooxien.de");
	}
}
