package eu.the5zig.mod.server.hypixel;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class HypixelQuakeListener extends AbstractGameListener<ServerHypixel.Quake> {

	@Override
	public Class<ServerHypixel.Quake> getGameMode() {
		return ServerHypixel.Quake.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("quakelobby");
	}

	@Override
	public void onMatch(ServerHypixel.Quake gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000);
			}
			if (key.equals("start")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("quake.kill.1") || key.equals("quake.kill.2")) {
				if (match.get(0).equals(The5zigMod.getDataManager().getUsername())) {
					gameMode.setKills(gameMode.getKills() + 1);
					gameMode.setKillStreak(gameMode.getKillStreak() + 1);
				}
				if (match.get(1).equals(The5zigMod.getDataManager().getUsername())) {
					gameMode.setDeaths(gameMode.getDeaths() + 1);
					gameMode.setKillStreak(0);
				}
			}
		}
	}


	@Override
	public void onTick(ServerHypixel.Quake gameMode) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (gameMode.getTime() != -1 && gameMode.getTime() - System.currentTimeMillis() < 0) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
	}
}
