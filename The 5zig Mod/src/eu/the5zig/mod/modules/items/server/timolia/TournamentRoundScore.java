package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class TournamentRoundScore extends GameModeItem<ServerTimolia.PvP> {

	public TournamentRoundScore() {
		super(ServerTimolia.PvP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "1 | 1";
		}
		return getGameMode().getTournament() == null ? null : getGameMode().getTournament().getRoundWins() + " | " + getGameMode().getTournament().getRoundLoses();
	}

	@Override
	public String getTranslation() {
		return "ingame.score";
	}
}
