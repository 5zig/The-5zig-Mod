package eu.the5zig.mod.modules.items.server.hypixel;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.hypixel.ServerHypixel;

public class BlitzKit extends GameModeItem<ServerHypixel.Blitz> {

	public BlitzKit() {
		super(ServerHypixel.Blitz.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Starter Kit" : getGameMode().getKit();
	}

	@Override
	public String getTranslation() {
		return "ingame.kit";
	}
}
