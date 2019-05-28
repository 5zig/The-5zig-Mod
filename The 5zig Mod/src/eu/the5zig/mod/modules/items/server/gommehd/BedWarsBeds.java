package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class BedWarsBeds extends GameModeItem<ServerGommeHD.BedWars> {

	public BedWarsBeds() {
		super(ServerGommeHD.BedWars.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 5;
		}
		return getGameMode().getBeds() > 0 ? getGameMode().getBeds() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.beds";
	}
}
