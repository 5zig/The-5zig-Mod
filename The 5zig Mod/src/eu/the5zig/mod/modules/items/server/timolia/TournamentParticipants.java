package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class TournamentParticipants extends GameModeItem<ServerTimolia.PvP> {

	public TournamentParticipants() {
		super(ServerTimolia.PvP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 2;
		}
		return getGameMode().getTournament() == null || getGameMode().getTournament().getParticipants() == 0 ? null : getGameMode().getTournament().getParticipants();
	}

	@Override
	public String getTranslation() {
		return "ingame.participants";
	}
}
