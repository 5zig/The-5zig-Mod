package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class EnderGamesKit extends GameModeItem<ServerGommeHD.EnderGames> {

	public EnderGamesKit() {
		super(ServerGommeHD.EnderGames.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Archer" : getGameMode().getKit();
	}

	@Override
	public String getTranslation() {
		return "ingame.kit";
	}
}
