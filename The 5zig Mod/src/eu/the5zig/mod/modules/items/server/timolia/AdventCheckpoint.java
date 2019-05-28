package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class AdventCheckpoint extends GameModeItem<ServerTimolia.Advent> {

	public AdventCheckpoint() {
		super(ServerTimolia.Advent.class, GameState.GAME, GameState.FINISHED);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 1;
		}
		return getGameMode().getCurrentCheckpoint();
	}

	@Override
	public String getTranslation() {
		return "ingame.checkpoint";
	}
}
