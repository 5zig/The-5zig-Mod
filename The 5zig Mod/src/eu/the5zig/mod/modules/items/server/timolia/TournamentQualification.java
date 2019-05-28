package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class TournamentQualification extends GameModeItem<ServerTimolia.PvP> {

	public TournamentQualification() {
		super(ServerTimolia.PvP.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 2;
		}
		return getGameMode().getTournament() == null || getGameMode().getTournament().getQualificationRounds() == 0 ||
				getGameMode().getTournament().getCurrentRound() > getGameMode().getTournament().getQualificationRounds() ? null :
				getGameMode().getTournament().getQualificationRounds() + " " + I18n.translate("ingame.qualification.rounds");
	}

	@Override
	public String getTranslation() {
		return "ingame.qualification";
	}
}
