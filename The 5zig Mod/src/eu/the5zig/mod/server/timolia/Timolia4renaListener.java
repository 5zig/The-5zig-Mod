package eu.the5zig.mod.server.timolia;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class Timolia4renaListener extends AbstractGameListener<ServerTimolia.Arena> {

	@Override
	public Class<ServerTimolia.Arena> getGameMode() {
		return ServerTimolia.Arena.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("4rena");
	}

	@Override
	public void onMatch(ServerTimolia.Arena gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("starting.actionbar")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("start")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
				//				gameMode.setTime(System.currentTimeMillis() + 1000 * 46);
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("4rena.round")) {
				gameMode.setRound(Integer.parseInt(match.get(0)));
				//				gameMode.setState(GameState.GAME);
				//				gameMode.setTime(System.currentTimeMillis() + 1000 * (60 * 5 + 1));
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("4rena.win")) {
				gameMode.setWinner(match.get(0));
				//				gameMode.setState(GameState.STARTING);
				//				gameMode.setTime(System.currentTimeMillis() + 1000 * 46);
			}
		}
	}

	@Override
	public void onTick(ServerTimolia.Arena gameMode) {
		if (gameMode.getState() == GameState.GAME) {
			if (gameMode.getTime() - System.currentTimeMillis() < 1000 * 30) {
				gameMode.setWinner(null);
			}
		}
	}

}
