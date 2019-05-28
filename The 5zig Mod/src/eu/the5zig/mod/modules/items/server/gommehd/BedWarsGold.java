package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class BedWarsGold extends GameModeItem<ServerGommeHD.BedWars> {

	public BedWarsGold() {
		super(ServerGommeHD.BedWars.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? shorten(10.0) : shorten(
				getGameMode().getGoldTime() - ((double) ((System.currentTimeMillis() - getGameMode().getTime()) % (getGameMode().getGoldTime() * 1000)) / 1000.0));
	}

	@Override
	public String getTranslation() {
		return "ingame.gold";
	}
}
