package eu.the5zig.mod.server.timolia;

import eu.the5zig.mod.server.ServerInstance;

import java.util.Locale;

public class ServerInstanceTimolia extends ServerInstance {

	@Override
	public void registerListeners() {
		getGameListener().registerListener(new TimoliaListener());
		getGameListener().registerListener(new Timolia4renaListener());
		getGameListener().registerListener(new TimoliaDNAListener());
		getGameListener().registerListener(new TimoliaPvPListener());
		getGameListener().registerListener(new TimoliaSplunListener());
		getGameListener().registerListener(new TimoliaBrainBowListener());
		getGameListener().registerListener(new TimoliaTSpieleListener());
		getGameListener().registerListener(new TimoliaInTimeListener());
		getGameListener().registerListener(new TimoliaArcadeListener());
		getGameListener().registerListener(new TimoliaAdventListener());
		getGameListener().registerListener(new TimoliaJumpWorldListener());
	}

	@Override
	public String getName() {
		return "Timolia.de";
	}

	@Override
	public String getConfigName() {
		return "timolia";
	}

	@Override
	public boolean handleServer(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("timolia.de");
	}

}
