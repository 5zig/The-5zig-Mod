package eu.the5zig.mod.server.venicraft;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class VenicraftMineathlonListener extends AbstractGameListener<ServerVenicraft.Mineathlon> {

	private boolean winAnnounced = false;

	@Override
	public Class<ServerVenicraft.Mineathlon> getGameMode() {
		return ServerVenicraft.Mineathlon.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("ma");
	}

	@Override
	public void onMatch(ServerVenicraft.Mineathlon gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("lobby.start")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("lobby.stop")) {
				gameMode.setTime(-1);
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("mineathlon.discipline")) {
				gameMode.setDiscipline(Integer.parseInt(match.get(0)) + " (" + match.get(1) + ")");
				gameMode.setRound(0);
			}
			if (key.equals("mineathlon.round")) {
				gameMode.setRound(Integer.parseInt(match.get(0)));
			}
			if (key.equals("mineathlon.invincibility.min") || key.equals("mineathlon.invincibility.sec")) {
				gameMode.setState(GameState.STARTING);
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000 * (key.equals("mineathlon.invincibility.min") ? 60 : 1));
			}
		}
		if (gameMode.getState() == GameState.STARTING) {
			if (key.equals("mineathlon.invincibility.min") || key.equals("mineathlon.invincibility.sec")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000 * (key.equals("mineathlon.invincibility.min") ? 60 : 1));
			}
			if (key.equals("mineathlon.no_invincibility")) {
				gameMode.setState(GameState.ENDGAME);
			}
		}
		if (gameMode.getState() == GameState.ENDGAME) {
			if (key.equals("mineathlon.kill")) {
				if (The5zigMod.getDataManager().getUsername().equals(match.get(1))) {
					gameMode.setKills(gameMode.getKills() + 1);
				}
			}
			if (key.equals("mineathlon.win.announcement")) {
				winAnnounced = true;
			}
			if (winAnnounced && key.equals("mineathlon.win.player")) {
				winAnnounced = false;
				gameMode.setWinner(match.get(0));
				gameMode.setState(GameState.FINISHED);
			}
		}
	}

	@Override
	public void onTick(ServerVenicraft.Mineathlon gameMode) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (gameMode.getTime() != -1 && System.currentTimeMillis() - gameMode.getTime() > 0) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
	}
}
