package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class ArcadeCurrentMinigame extends GameModeItem<ServerTimolia.Arcade> {

	public ArcadeCurrentMinigame() {
		super(ServerTimolia.Arcade.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "PvP";
		}
		return getGameMode().getCurrentMiniGame();
	}

	@Override
	public String getTranslation() {
		return "ingame.current_minigame";
	}
}
