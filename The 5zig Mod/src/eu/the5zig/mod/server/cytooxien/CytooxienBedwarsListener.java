package eu.the5zig.mod.server.cytooxien;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class CytooxienBedwarsListener extends AbstractGameListener<ServerCytooxien.Bedwars> {

	public Class<ServerCytooxien.Bedwars> getGameMode() {
		return ServerCytooxien.Bedwars.class;
	}

	public boolean matchLobby(String lobby) {
		return lobby.contains("Bedwars");
	}

	@Override
	public void onMatch(ServerCytooxien.Bedwars gameMode, String key, IPatternResult match) {
		if (key.equals("bedwars.team")) {
			gameMode.setTeam(match.get(0));
			return;
		}
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("bedwars.gamestart")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		} else if (gameMode.getState() == GameState.GAME) {
			if (key.equals("bedwars.death")) {
				if (match.get(0).equals(The5zigMod.getDataManager().getUsername())) {
					gameMode.setDeaths(gameMode.getDeaths() + 1);
				}
			} else if (key.equals("bedwars.eliminated")) {
				if (match.get(1).equals(The5zigMod.getDataManager().getUsername())) {
					gameMode.realkills++;
				}
			} else if (key.equals("bedwars.kill")) {
				gameMode.setKills(gameMode.getKills() + 1);
			} else if (key.equals("bedwars.died")) {
				gameMode.setDeaths(gameMode.getDeaths() + 1);
			} else if (key.equals("bedwars.beddestroyed")) {
				if (match.get(1).equals(The5zigMod.getDataManager().getUsername())) {
					gameMode.bedsdestroyed++;
				}
				if (match.get(0).equals(gameMode.getTeam())) {
					gameMode.setCanRespawn(false);
				}
			}
		}
	}
}