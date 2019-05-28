package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class MarioPartyCurrentMinigame extends GameModeItem<ServerCytooxien.MarioParty> {

	public MarioPartyCurrentMinigame() {
		super(ServerCytooxien.MarioParty.class, GameState.LOBBY, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Ampelrennen" : getGameMode().getMinigameQueue().isEmpty() ? null : getGameMode().getMinigameQueue().get(0);
	}

	@Override
	public String getTranslation() {
		return "ingame.current_minigame";
	}
}
