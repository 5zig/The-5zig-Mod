package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.modules.LargeTextItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class InTimeInvincibilityMessage extends LargeTextItem<ServerTimolia.InTime> {

	public InTimeInvincibilityMessage() {
		super(ServerTimolia.InTime.class, GameState.GAME);
	}

	@Override
	protected String getText() {
		if (getGameMode().getInvincibleTimer() != -1 && getGameMode().getInvincibleTimer() - System.currentTimeMillis() > 0) {
			return I18n.translate("ingame.no_longer_invincible");
		}
		if (getGameMode().getSpawnRegenerationTimer() != -1 && getGameMode().getSpawnRegenerationTimer() - System.currentTimeMillis() > 0) {
			return I18n.translate("ingame.now_spawn_regeneration");
		}
		if (getGameMode().getLootTimer() != -1 && getGameMode().getLootTimer() - System.currentTimeMillis() > 0) {
			return I18n.translate("ingame.loot_spawned");
		}
		return null;
	}

}
