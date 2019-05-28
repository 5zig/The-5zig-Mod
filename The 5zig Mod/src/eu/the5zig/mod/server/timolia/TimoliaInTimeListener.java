package eu.the5zig.mod.server.timolia;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.IPatternResult;

public class TimoliaInTimeListener extends AbstractGameListener<ServerTimolia.InTime> {

	@Override
	public Class<ServerTimolia.InTime> getGameMode() {
		return ServerTimolia.InTime.class;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return lobby.startsWith("intime");
	}

	@Override
	public void onMatch(ServerTimolia.InTime gameMode, String key, IPatternResult match) {
		if (gameMode.getState() == GameState.LOBBY) {
			if (key.equals("starting.actionbar")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("start")) {
				gameMode.setState(GameState.STARTING);
			}
		}
		if (gameMode.getState() == GameState.STARTING) {
			if (key.equals("intime.starting")) {
				gameMode.setTime(System.currentTimeMillis() + Integer.parseInt(match.get(0)) * 1000L);
			}
			if (key.equals("intime.start")) {
				gameMode.setState(GameState.GAME);
				gameMode.setTime(System.currentTimeMillis());
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("intime.invincibility")) {
				gameMode.setInvincible(false);
				gameMode.setInvincibleTimer(System.currentTimeMillis() + 3000);
			}
			if (key.equals("intime.loot.min")) {
				gameMode.setLoot(System.currentTimeMillis() + 1000 * 60 * Integer.parseInt(match.get(0)));
			}
			if (key.equals("intime.loot.sec")) {
				gameMode.setLoot(System.currentTimeMillis() + 1000 * Integer.parseInt(match.get(0)));
			}
			if (key.equals("intime.loot.spawned")) {
				gameMode.setLoot(-1);
				gameMode.setLootTimer(System.currentTimeMillis() + 3000);
			}
			if (key.equals("intime.spawn_regeneration")) {
				gameMode.setSpawnRegeneration(true);
				gameMode.setSpawnRegenerationTimer(System.currentTimeMillis() + 3000);
			}
		}
	}

	@Override
	public void onTick(ServerTimolia.InTime gameMode) {
		if (gameMode.getState() == GameState.GAME) {
			if (gameMode.getInvincibleTimer() != -1 && gameMode.getInvincibleTimer() - System.currentTimeMillis() < 0) {
				gameMode.setInvincibleTimer(-1);
			}
			if (gameMode.getSpawnRegenerationTimer() != -1 && gameMode.getSpawnRegenerationTimer() - System.currentTimeMillis() < 0) {
				gameMode.setSpawnRegenerationTimer(-1);
			}
			if (gameMode.getLootTimer() != -1 && gameMode.getLootTimer() - System.currentTimeMillis() < 0) {
				gameMode.setLootTimer(-1);
			}
		}
	}
}
