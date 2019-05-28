package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;

public class EntityCount extends StringItem {
	@Override
	protected Object getValue(boolean dummy) {
		return The5zigMod.getVars().getEntityCount();
	}

	@Override
	public String getTranslation() {
		return "ingame.entities";
	}
}
