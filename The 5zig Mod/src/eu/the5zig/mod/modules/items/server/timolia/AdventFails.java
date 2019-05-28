package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class AdventFails extends GameModeItem<ServerTimolia.Advent> {

	public AdventFails() {
		super(ServerTimolia.Advent.class, GameState.GAME, GameState.FINISHED);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 99;
		}
		return getGameMode().getFails();
	}

	@Override
	public String getTranslation() {
		return "ingame.fails";
	}
}
