package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class SkyWarsKit extends GameModeItem<ServerGommeHD.SkyWars> {

	public SkyWarsKit() {
		super(ServerGommeHD.SkyWars.class);
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
