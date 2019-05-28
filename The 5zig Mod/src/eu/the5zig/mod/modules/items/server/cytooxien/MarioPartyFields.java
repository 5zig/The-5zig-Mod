package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class MarioPartyFields extends GameModeItem<ServerCytooxien.MarioParty> {

	public MarioPartyFields() {
		super(ServerCytooxien.MarioParty.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "35" : getGameMode().getRemainingFields() != -20 ? getGameMode().getRemainingFields() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.remaining_fields";
	}
}
