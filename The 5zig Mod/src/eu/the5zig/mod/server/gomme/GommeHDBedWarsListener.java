package eu.the5zig.mod.server.gomme;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameServer;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class GommeHDBedWarsListener extends AbstractGameListener<ServerGommeHD.BedWars> {

	@Override
	public Class<ServerGommeHD.BedWars> getGameMode() {
		return ServerGommeHD.BedWars.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.equals("BedWars") || lobby.equals("BW") || lobby.equals("QBW");
	}

	@Override
	public void onGameModeJoin(ServerGommeHD.BedWars gameMode) {
		gameMode.setGoldTime(((GameServer) The5zigMod.getDataManager().getServer()).getLobby().equals("QBW") ? 15 : 30);
	}

	@Override
	public void onMatch(ServerGommeHD.BedWars gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("bw.lobby.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("bw.start")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("bw.bed.self")) {
				gameMode.setCanRespawn(false);
			}
			if (key.equals("bw.bed.other") && match.get(0).equals(The5zigMod.getDataManager().getUsername())) {
				gameMode.setBeds(gameMode.getBeds() + 1);
			}
			if (key.equals("bw.win")) {
				gameMode.setWinner(match.get(0));
				gameMode.setState(GameState.FINISHED);
			}
		}
		if (key.equals("bw.team")) {
			gameMode.setTeam(match.get(0));
		}
	}

}
