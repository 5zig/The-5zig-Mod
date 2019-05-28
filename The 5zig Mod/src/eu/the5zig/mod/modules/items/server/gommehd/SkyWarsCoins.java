package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class SkyWarsCoins extends GameModeItem<ServerGommeHD.SkyWars> {

	public SkyWarsCoins() {
		super(ServerGommeHD.SkyWars.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "51.081";
		}
		return getGameMode().getCoins();
	}

	@Override
	public String getTranslation() {
		return "ingame.coins";
	}
}
