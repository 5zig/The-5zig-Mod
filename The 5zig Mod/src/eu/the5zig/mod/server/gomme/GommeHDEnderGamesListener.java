package eu.the5zig.mod.server.gomme;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class GommeHDEnderGamesListener extends AbstractGameListener<ServerGommeHD.EnderGames> {

	@Override
	public Class<ServerGommeHD.EnderGames> getGameMode() {
		return ServerGommeHD.EnderGames.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.equals("EnderGames") || lobby.equals("EG");
	}

	@Override
	public void onGameModeJoin(ServerGommeHD.EnderGames gameMode) {
		getGameListener().sendAndIgnore("/coins", "eg.coins");
	}

	@Override
	public void onMatch(ServerGommeHD.EnderGames gameMode, String key, IPatternResult match) {
		if (key.equals("eg.coins")) {
			gameMode.setCoins(match.get(0));
		}
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("eg.lobby.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("eg.lobby.start")) {
				gameMode.setState(GameState.STARTING);
			}
		}
		if (gameMode.getState() == GameState.LOBBY || gameMode.getState() == GameState.STARTING) {
			if (key.equals("eg.kit")) {
				gameMode.setKit(match.get(0));
			}
		}
		if (gameMode.getState() == GameState.STARTING) {
			if (key.equals("eg.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("eg.start")) {
				gameMode.setState(GameState.PREGAME);
				gameMode.setTime(System.currentTimeMillis() + 1000 * 22);
			}
		}
		if (gameMode.getState() == GameState.PREGAME) {
			if (key.equals("eg.invincibility")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("eg.invincibility_off")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis() - 1000 * 74);
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("eg.win")) {
				gameMode.setWinner(match.get(0));
				gameMode.setState(GameState.FINISHED);
			}
		}
		if (gameMode.getState() == GameState.PREGAME || gameMode.getState() == GameState.GAME) {
			if (key.equals("eg.track")) {
				if (The5zigMod.getConfig().getBool("showCompassTarget")) {
					The5zigMod.getGuiIngame().showTextAboveHotbar("Tracking: " + match.get(0) + " (" + match.get(1) + ")");
				}
			}
		}
	}

}
