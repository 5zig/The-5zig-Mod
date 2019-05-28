package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;

public class BedwarsBeds extends GameModeItem<ServerCytooxien.Bedwars> {

	public BedwarsBeds() {
		super(ServerCytooxien.Bedwars.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 2;
		}
		return getGameMode().bedsdestroyed > 0  ? getGameMode().bedsdestroyed : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.beds";
	}
}