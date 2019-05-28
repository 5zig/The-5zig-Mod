package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.timolia.ServerTimolia;

public class BrainbowTeam extends GameModeItem<ServerTimolia.BrainBow> {

	public BrainbowTeam() {
		super(ServerTimolia.BrainBow.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "Red";
		}
		return getGameMode().getTeam();
	}

	@Override
	public String getTranslation() {
		return "ingame.team";
	}
}
