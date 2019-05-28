package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class ArcadeNextMinigame extends GameModeItem<ServerTimolia.Arcade> {

	public ArcadeNextMinigame() {
		super(ServerTimolia.Arcade.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "PvP";
		}
		return getGameMode().getNextMiniGame();
	}

	@Override
	public String getTranslation() {
		return "ingame.next_minigame";
	}
}
