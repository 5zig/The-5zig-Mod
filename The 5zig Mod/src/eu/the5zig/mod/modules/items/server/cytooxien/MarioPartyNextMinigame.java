package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class MarioPartyNextMinigame extends GameModeItem<ServerCytooxien.MarioParty> {

	public MarioPartyNextMinigame() {
		super(ServerCytooxien.MarioParty.class, GameState.LOBBY, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Pferderennen" : getGameMode().getMinigameQueue().size() <= 1 ? null : getGameMode().getMinigameQueue().get(1);
	}

	@Override
	public String getTranslation() {
		return "ingame.next_minigame";
	}
}
