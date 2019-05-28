package eu.the5zig.mod.modules.items.server.hypixel;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.hypixel.ServerHypixel;

public class BlitzStar extends GameModeItem<ServerHypixel.Blitz> {

	public BlitzStar() {
		super(ServerHypixel.Blitz.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return shorten(10.0);
		}
		if (getGameMode().getWinner() == null && getGameMode().getStar() != -1 && getGameMode().getStar() - System.currentTimeMillis() > 0) {
			return shorten((getGameMode().getStar() - System.currentTimeMillis()) / 1000.0);
		}
		return null;
	}

	@Override
	public String getTranslation() {
		return "ingame.star";
	}
}
