package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class InTimeLoot extends GameModeItem<ServerTimolia.InTime> {

	public InTimeLoot() {
		super(ServerTimolia.InTime.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return shorten(30.0);
		}
		return getGameMode().getLoot() != -1 && getGameMode().getLoot() - System.currentTimeMillis() > 0 ? shorten((getGameMode().getLoot() - System.currentTimeMillis()) / 1000.0) : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.loot";
	}
}
