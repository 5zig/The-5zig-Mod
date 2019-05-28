package eu.the5zig.mod.server.gomme;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.IPatternResult;

public class GommeHDPvPFFAListener extends AbstractGameListener<ServerGommeHD.FFA> {

	@Override
	public Class<ServerGommeHD.FFA> getGameMode() {
		return ServerGommeHD.FFA.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.equals("CLASSIC") || lobby.equals("HARDCORE") || lobby.equals("SOUP") || lobby.equals("GUNGAME") || lobby.equals("Surf");
	}

	@Override
	public void onMatch(ServerGommeHD.FFA gameMode, String key, IPatternResult match) {
		if (key.equals("ffa.kill")) {
			gameMode.setKillStreak(gameMode.getKillStreak() + 1);
		}
		if (key.equals("ffa.death")) {
			gameMode.setKillStreak(0);
		}
	}
}
