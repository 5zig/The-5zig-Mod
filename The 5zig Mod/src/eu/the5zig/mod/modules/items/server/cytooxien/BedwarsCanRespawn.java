package eu.the5zig.mod.modules.items.server.cytooxien;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.cytooxien.ServerCytooxien;
import eu.the5zig.util.minecraft.ChatColor;

public class BedwarsCanRespawn extends GameModeItem<ServerCytooxien.Bedwars> {

	public BedwarsCanRespawn() {
		super(ServerCytooxien.Bedwars.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? ChatColor.GREEN + "\u2714" : getGameMode().isCanRespawn() ? ChatColor.GREEN + "\u2714" :
				ChatColor.RED + "\u2718";
	}

	@Override
	public String getTranslation() {
		return "ingame.can_respawn";
	}
}