package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;

public class Biome extends StringItem {
	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Forest" : The5zigMod.getVars().getBiome();
	}

	@Override
	public String getTranslation() {
		return "ingame.biome";
	}
}
