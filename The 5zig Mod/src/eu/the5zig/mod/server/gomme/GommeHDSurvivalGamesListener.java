package eu.the5zig.mod.server.gomme;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class GommeHDSurvivalGamesListener extends AbstractGameListener<ServerGommeHD.SurvivalGames> {

	@Override
	public Class<ServerGommeHD.SurvivalGames> getGameMode() {
		return ServerGommeHD.SurvivalGames.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.equals("SurvivalGames") || lobby.equals("Quick SurvivalGames");
	}

	@Override
	public void onMatch(ServerGommeHD.SurvivalGames gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("sg.lobby.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("sg.lobby.start")) {
				gameMode.setState(GameState.STARTING);
			}
		}
		if (gameMode.getState() == GameState.STARTING || gameMode.getState() == GameState.LOBBY) {
			if (key.equals("sg.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("sg.start")) {
				gameMode.setState(GameState.PREGAME);
				gameMode.setTime(System.currentTimeMillis() + 1000 * 22);
			}
		}
		if (gameMode.getState() == GameState.PREGAME) {
			if (key.equals("sg.invincibility")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("sg.deathmatch")) {
				gameMode.setDeathmatchTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000);
			}
			if (key.equals("sg.win")) {
				gameMode.setWinner(match.get(0));
				gameMode.setState(GameState.FINISHED);
			}
		}
	}

}
