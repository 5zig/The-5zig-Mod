package eu.the5zig.mod.modules.items.server.hypixel;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.hypixel.ServerHypixel;

public class BlitzDeathmatch extends GameModeItem<ServerHypixel.Blitz> {

	public BlitzDeathmatch() {
		super(ServerHypixel.Blitz.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return shorten(10.0);
		}
		if (getGameMode().getWinner() == null && getGameMode().getDeathmatch() != -1 && getGameMode().getDeathmatch() - System.currentTimeMillis() > 0) {
			return shorten((getGameMode().getDeathmatch() - System.currentTimeMillis()) / 1000.0);
		}
		return null;
	}

	@Override
	public String getTranslation() {
		return "ingame.deathmatch";
	}
}
