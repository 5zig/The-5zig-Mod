package eu.the5zig.mod.modules.items.server.venicraft;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.venicraft.ServerVenicraft;

public class MineathlonRound extends GameModeItem<ServerVenicraft.Mineathlon> {

	public MineathlonRound() {
		super(ServerVenicraft.Mineathlon.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 1;
		}
		return getGameMode().getRound() > 0 ? getGameMode().getRound() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.round";
	}
}
