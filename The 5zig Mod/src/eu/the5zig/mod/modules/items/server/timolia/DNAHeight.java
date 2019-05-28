package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class DNAHeight extends GameModeItem<ServerTimolia.DNA> {

	public DNAHeight() {
		super(ServerTimolia.DNA.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 16;
		}
		return shorten(getGameMode().getHeight()) + "/" + shorten(32.0);
	}

	@Override
	public String getTranslation() {
		return "ingame.height";
	}
}
