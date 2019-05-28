package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class SkyWarsTeam extends GameModeItem<ServerGommeHD.SkyWars> {

	public SkyWarsTeam() {
		super(ServerGommeHD.SkyWars.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 2;
		}
		return getGameMode().getTeam() > 0 ? getGameMode().getTeam() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.team";
	}
}
