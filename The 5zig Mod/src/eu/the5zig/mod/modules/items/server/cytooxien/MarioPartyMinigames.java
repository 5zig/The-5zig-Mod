package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class MarioPartyMinigames extends GameModeItem<ServerCytooxien.MarioParty> {

	public MarioPartyMinigames() {
		super(ServerCytooxien.MarioParty.class, GameState.GAME, GameState.FINISHED);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? 6 : getGameMode().minigames;
	}

	@Override
	public String getTranslation() {
		return "Minigames";
	}
}