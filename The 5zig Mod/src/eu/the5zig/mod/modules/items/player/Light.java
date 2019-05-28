package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;

public class Light extends StringItem {

	@Override
	public void registerSettings() {
		getProperties().addSetting("lightLevelPercentage", true);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? format(14) : format(The5zigMod.getVars().getLightLevel());
	}

	private String format(int lightLevel) {
		return (Boolean) getProperties().getSetting("lightLevelPercentage").get() ? shorten((float) lightLevel / 15f * 100f) + "%" : lightLevel + "/15";
	}

	@Override
	public String getTranslation() {
		return "ingame.light_level";
	}
}
