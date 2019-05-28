package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class MarioPartyFirstSecondThird extends GameModeItem<ServerCytooxien.MarioParty> {

	public MarioPartyFirstSecondThird() {
		super(ServerCytooxien.MarioParty.class, GameState.GAME, GameState.FINISHED);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "1/2/0" : getGameMode().first + "/" + getGameMode().second + "/" + getGameMode().third;
	}

	@Override
	public String getTranslation() {
		return "#1/#2/#3";
	}
}