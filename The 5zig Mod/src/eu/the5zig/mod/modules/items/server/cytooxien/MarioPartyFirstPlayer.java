package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class MarioPartyFirstPlayer extends GameModeItem<ServerCytooxien.MarioParty> {

	public MarioPartyFirstPlayer() {
		super(ServerCytooxien.MarioParty.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "5zig" : getGameMode().getFirstPlayer();
	}

	@Override
	public String getTranslation() {
		return "ingame.first_player";
	}
}
