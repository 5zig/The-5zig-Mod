package eu.the5zig.mod.server.timolia;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class TimoliaSplunListener extends AbstractGameListener<ServerTimolia.Splun> {

	@Override
	public Class<ServerTimolia.Splun> getGameMode() {
		return ServerTimolia.Splun.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("splun");
	}

	@Override
	public void onMatch(ServerTimolia.Splun gameMode, String key, IPatternResult match) {
		if (key.equals("starting.actionbar")) {
			gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
		}
		if (key.equals("start")) {
			gameMode.setState(GameState.GAME);
			gameMode.setTime(System.currentTimeMillis());
		}
	}

}
