package eu.the5zig.mod.server.playminity;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class PlayMinityJumpLeagueListener extends AbstractGameListener<ServerPlayMinity.JumpLeague> {

	private double lastY;

	@Override
	public Class<ServerPlayMinity.JumpLeague> getGameMode() {
		return ServerPlayMinity.JumpLeague.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("JL");
	}

	@Override
	public void onMatch(ServerPlayMinity.JumpLeague gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("jumpleague.lobby.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000);
			}
			if (key.equals("jumpleague.lobby.start")) {
				gameMode.setState(GameState.STARTING);
				gameMode.setTime(System.currentTimeMillis() + 20 * 1000);
			}
		}
		if (gameMode.getState() == GameState.STARTING) {
			if (key.equals("jumpleague.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000);
			}
			if (key.equals("jumpleague.start")) {
				lastY = Integer.MAX_VALUE;
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("jumpleague.checkpoint")) {
				gameMode.setCheckPoint(Integer.parseInt(match.get(0)));
				gameMode.setMaxCheckPoints(Integer.parseInt(match.get(1)));
			}
			if (key.equals("jumpleague.endmatch")) {
				long time = gameMode.getTime();
				gameMode.setState(GameState.ENDGAME);
				gameMode.setTime(time);
			}
		}
		if (gameMode.getState() == GameState.ENDGAME) {
			if (key.equals("jumpleague.kill")) {
				if (match.get(0).equals(The5zigMod.getDataManager().getUsername())) {
					gameMode.setLives(gameMode.getLives() - 1);
				}
				if (match.get(1).equals(The5zigMod.getDataManager().getUsername())) {
					gameMode.setKills(gameMode.getKills() + 1);
				}
			}
			if (key.equals("jumpleague.lives")) {
				gameMode.setLives(Integer.parseInt(match.get(0)));
			}
			if (key.equals("jumpleague.win")) {
				gameMode.setWinner(match.get(0));
				gameMode.setState(GameState.FINISHED);
			}
		}
	}

	@Override
	public void onTick(ServerPlayMinity.JumpLeague gameMode) {
		if (gameMode.getState() == GameState.GAME) {
			if (lastY <= 45 && The5zigMod.getVars().getPlayerPosY() - lastY > 3) {
				gameMode.setFails(gameMode.getFails() + 1);
			}
			lastY = The5zigMod.getVars().getPlayerPosY();
		}
	}
}
