package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;

public class FPS extends StringItem {

	@Override
	public void registerSettings() {
		getProperties().addSetting("preciseFPS", true);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? 120 : (Boolean) getProperties().getSetting("preciseFPS").get() ? The5zigMod.getDataManager().getFpsCalculator().getCurrentFPS() : The5zigMod.getVars().getFPS();
	}

	@Override
	public String getTranslation() {
		return "ingame.fps";
	}
}
