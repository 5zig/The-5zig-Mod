package eu.the5zig.mod.server.hypixel;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

public class HypixelPaintballListener extends AbstractGameListener<ServerHypixel.Paintball> {

	@Override
	public Class<ServerHypixel.Paintball> getGameMode() {
		return ServerHypixel.Paintball.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("paintballlobby");
	}

	@Override
	public void onMatch(ServerHypixel.Paintball gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000);
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("paintball.kill")) {
				gameMode.setKills(gameMode.getKills() + 1);
			}
			if (key.equals("paintball.death")) {
				gameMode.setDeaths(gameMode.getDeaths() + 1);
			}
		}
		if (key.equals("paintball.team")) {
			gameMode.setTeam(WordUtils.capitalize(match.get(0).toLowerCase(Locale.ROOT)));
		}
	}

	@Override
	public void onTick(ServerHypixel.Paintball gameMode) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (gameMode.getTime() != -1 && gameMode.getTime() - System.currentTimeMillis() < 0) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
	}
}
