package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class MarioPartyPlace extends GameModeItem<ServerCytooxien.MarioParty> {

	public MarioPartyPlace() {
		super(ServerCytooxien.MarioParty.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "#1" : getGameMode().getPlace() > 0 ? "#" + getGameMode().getPlace() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.place";
	}
}
