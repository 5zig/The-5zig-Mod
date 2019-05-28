package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class BrainbowScore extends GameModeItem<ServerTimolia.BrainBow> {

	public BrainbowScore() {
		super(ServerTimolia.BrainBow.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 0;
		}
		return getGameMode().getScore() > 0 ? getGameMode().getScore() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.scores";
	}
}
