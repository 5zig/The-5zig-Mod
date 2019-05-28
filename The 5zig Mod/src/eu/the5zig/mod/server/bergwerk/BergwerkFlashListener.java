package eu.the5zig.mod.server.bergwerk;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class BergwerkFlashListener extends AbstractGameListener<ServerBergwerk.Flash> {

	@Override
	public Class<ServerBergwerk.Flash> getGameMode() {
		return ServerBergwerk.Flash.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("FLASH");
	}

	@Override
	public void onMatch(ServerBergwerk.Flash gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("start")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("flash.countdown")) {
				gameMode.setState(GameState.ENDGAME);
				gameMode.setTime(System.currentTimeMillis() + 60 * 1000);
			}
		}
		if (gameMode.getState() == GameState.GAME || gameMode.getState() == GameState.ENDGAME) {
			if (key.equals("flash.win")) {
				gameMode.setWinner(match.get(0));
				gameMode.setState(GameState.FINISHED);
			}
		}
	}

	@Override
	public void onTick(ServerBergwerk.Flash gameMode) {
		if (gameMode.getState() == GameState.ENDGAME) {
			if (System.currentTimeMillis() - gameMode.getTime() > 0)
				gameMode.setTime(-1);
		}
	}
}
