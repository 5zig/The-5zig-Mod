package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class ArenaRound extends GameModeItem<ServerTimolia.Arena> {

	public ArenaRound() {
		super(ServerTimolia.Arena.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 1;
		}
		return getGameMode().getRound();
	}

	@Override
	public String getTranslation() {
		return "ingame.round";
	}
}
