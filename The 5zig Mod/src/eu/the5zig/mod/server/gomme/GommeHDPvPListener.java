package eu.the5zig.mod.server.gomme;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class GommeHDPvPListener extends AbstractGameListener<ServerGommeHD.PvPMatch> {

	@Override
	public Class<ServerGommeHD.PvPMatch> getGameMode() {
		return ServerGommeHD.PvPMatch.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("1VS1");
	}

	@Override
	public void onMatch(ServerGommeHD.PvPMatch gameMode, String key, IPatternResult match) {
		if (key.equals("pvp.starting")) {
			gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
		}
		if (key.equals("pvp.start")) {
			gameMode.setState(GameState.GAME);
			gameMode.setTime(System.currentTimeMillis());
		}
		if (key.equals("pvp.win")) {
			gameMode.setWinner(match.get(0));
			gameMode.setState(GameState.FINISHED);
		}
	}

	@Override
	public void onTick(ServerGommeHD.PvPMatch gameMode) {
		if (gameMode.getState() == GameState.LOBBY) {
			gameMode.setState(GameState.STARTING);
		}
	}

}
