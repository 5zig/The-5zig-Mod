package eu.the5zig.mod.server.gomme;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class GommeHDRageModeListener extends AbstractGameListener<ServerGommeHD.RageMode> {

	@Override
	public Class<ServerGommeHD.RageMode> getGameMode() {
		return ServerGommeHD.RageMode.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.equals("RageMode");
	}

	@Override
	public void onMatch(ServerGommeHD.RageMode gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("rm.lobby.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("rm.lobby.start")) {
				gameMode.setState(GameState.STARTING);
			}
		}
		if (gameMode.getState() == GameState.STARTING) {
			if (key.equals("rm.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("rm.start")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("rm.kill")) {
				gameMode.setKills(gameMode.getKills() + 1);
				gameMode.setKillStreak(gameMode.getKillStreak() + 1);
				gameMode.setEmeralds(gameMode.getEmeralds() + 1);
			}
			if (key.equals("rm.kill.nemesis")) {
				gameMode.setEmeralds(gameMode.getEmeralds() + 1);
			}
			if (key.equals("rm.death.axe") || key.equals("rm.death.suicide")) {
				gameMode.setDeaths(gameMode.getDeaths() + 1);
				gameMode.setKillStreak(0);
			}
			if (key.equals("rm.shop")) {
				String item = match.get(0);
				if (item.equals("Mine")) {
					gameMode.setEmeralds(gameMode.getEmeralds() - 5);
				} else if (item.equals("Hundestaffel") || item.equals("Rüstung")) {
					gameMode.setEmeralds(gameMode.getEmeralds() - 10);
				} else if (item.equals("Geschwindigkeit")) {
					gameMode.setEmeralds(gameMode.getEmeralds() - 15);
				} else if (item.equals("Airstrike")) {
					gameMode.setEmeralds(gameMode.getEmeralds() - 25);
				} else if (item.equals("Gottes Licht")) {
					gameMode.setEmeralds(gameMode.getEmeralds() - 30);
				}
			}
			if (key.equals("rm.win")) {
				gameMode.setWinner(match.get(0));
				gameMode.setState(GameState.FINISHED);
			}
		}
	}
}
