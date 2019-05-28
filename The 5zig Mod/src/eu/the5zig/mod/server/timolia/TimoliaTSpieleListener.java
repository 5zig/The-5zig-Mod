package eu.the5zig.mod.server.timolia;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class TimoliaTSpieleListener extends AbstractGameListener<ServerTimolia.TSpiele> {

	@Override
	public Class<ServerTimolia.TSpiele> getGameMode() {
		return ServerTimolia.TSpiele.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("tspiele");
	}

	@Override
	public void onMatch(ServerTimolia.TSpiele gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("starting.actionbar")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("start")) {
				gameMode.setState(GameState.STARTING);
			}
		}
		if (gameMode.getState() == GameState.STARTING) {
			if (key.equals("tspiele.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("tspiele.start")) {
				gameMode.setState(GameState.PREGAME);
				gameMode.setTime(System.currentTimeMillis() + 1000 * 61);
			}
		}
		if (gameMode.getState() == GameState.PREGAME) {
			if (key.equals("tspiele.invincibility")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000);
			}
			if (key.equals("tspiele.invincibility_off")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis() - 1000 * 61);
			}
		}
	}

}
