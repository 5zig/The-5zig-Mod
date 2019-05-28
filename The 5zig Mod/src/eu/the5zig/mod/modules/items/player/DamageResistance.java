package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;

public class DamageResistance extends StringItem {

	private float previousResistanceFactor;
	private long lastUpdated;

	@Override
	protected Object getValue(boolean dummy) {
		if (System.currentTimeMillis() - lastUpdated > 100) {
			lastUpdated = System.currentTimeMillis();
			previousResistanceFactor = 34.0f;
			if (!dummy) {
				previousResistanceFactor = The5zigMod.getVars().getResistanceFactor();
			}
		}
		return shorten(previousResistanceFactor) + "%";
	}

	@Override
	public String getTranslation() {
		return "ingame.damage_resistance";
	}
}
