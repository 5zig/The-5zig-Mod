package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class InTimeInvincibility extends GameModeItem<ServerTimolia.InTime> {

	public InTimeInvincibility() {
		super(ServerTimolia.InTime.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return The5zigMod.toBoolean(dummy || getGameMode().isInvincible());
	}

	@Override
	public String getTranslation() {
		return "ingame.invincibility";
	}
}
