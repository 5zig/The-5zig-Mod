package eu.the5zig.mod.modules.items.server.venicraft;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.venicraft.ServerVenicraft;

public class MineathlonDiscipline extends GameModeItem<ServerVenicraft.Mineathlon> {

	public MineathlonDiscipline() {
		super(ServerVenicraft.Mineathlon.class, GameState.STARTING, GameState.GAME, GameState.ENDGAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "1 (Jump'n'Run)";
		}
		return getGameMode().getDiscipline();
	}

	@Override
	public String getTranslation() {
		return "ingame.discipline";
	}
}
