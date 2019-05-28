package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class EnderGamesCoins extends GameModeItem<ServerGommeHD.EnderGames> {

	public EnderGamesCoins() {
		super(ServerGommeHD.EnderGames.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "10.415";
		}
		return getGameMode().getCoins();
	}

	@Override
	public String getTranslation() {
		return "ingame.coins";
	}
}
