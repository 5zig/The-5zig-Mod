package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class BedwarsTeam extends GameModeItem<ServerCytooxien.Bedwars> {

	public BedwarsTeam() {
		super(ServerCytooxien.Bedwars.class, GameState.GAME, GameState.LOBBY);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Rot" : getGameMode().getTeam();
	}

	@Override
	public String getTranslation() {
		return "ingame.team";
	}
}