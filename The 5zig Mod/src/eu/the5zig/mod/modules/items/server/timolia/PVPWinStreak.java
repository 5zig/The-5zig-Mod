package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class PVPWinStreak extends GameModeItem<ServerTimolia.PvP> {

	public PVPWinStreak() {
		super(ServerTimolia.PvP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 1;
		}
		return getGameMode().getWinStreak() > 0 ? getGameMode().getWinStreak() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.winstreak";
	}
}
