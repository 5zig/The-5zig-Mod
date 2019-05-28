package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class PVPOpponent extends GameModeItem<ServerTimolia.PvP> {

	public PVPOpponent() {
		super(ServerTimolia.PvP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "5zig";
		}
		return getGameMode().getOpponent();
	}

	@Override
	public String getTranslation() {
		return "ingame.opponent";
	}
}
