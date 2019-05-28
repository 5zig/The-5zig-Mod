package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class AdventParkour extends GameModeItem<ServerTimolia.Advent> {

	public AdventParkour() {
		super(ServerTimolia.Advent.class, GameState.GAME, GameState.FINISHED);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "Christmas";
		}
		return getGameMode().getParkourName();
	}

	@Override
	public String getTranslation() {
		return "ingame.parkour";
	}
}
