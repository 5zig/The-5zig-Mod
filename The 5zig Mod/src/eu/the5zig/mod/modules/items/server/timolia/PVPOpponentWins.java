package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class PVPOpponentWins extends GameModeItem<ServerTimolia.PvP> {

	public PVPOpponentWins() {
		super(ServerTimolia.PvP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 1;
		}
		return getGameMode().getOpponentStats() != null ? getGameMode().getOpponentStats().getGamesWon() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.opponent.games_won";
	}
}
