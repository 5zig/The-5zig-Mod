package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class BedWarsTeam extends GameModeItem<ServerGommeHD.BedWars> {

	public BedWarsTeam() {
		super(ServerGommeHD.BedWars.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Red" : getGameMode().getTeam();
	}

	@Override
	public String getTranslation() {
		return "ingame.team";
	}
}
