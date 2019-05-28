package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class TournamentFirstTo extends GameModeItem<ServerTimolia.PvP> {

	public TournamentFirstTo() {
		super(ServerTimolia.PvP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 3;
		}
		return getGameMode().getTournament() == null ? null :
				getGameMode().getTournament().getCurrentRound() <= getGameMode().getTournament().getQualificationRounds() ? getGameMode().getTournament().getQualificationFirstTo() :
						getGameMode().getTournament().getRoundFirstTo();
	}

	@Override
	public String getTranslation() {
		return "ingame.first_to";
	}
}
