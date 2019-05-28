package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;

public class Pitch extends StringItem {

	@Override
	protected Object getValue(boolean dummy) {
		return getF(dummy);
	}

	private String getF(boolean dummy) {
		float rotationPitch = dummy ? 0 : The5zigMod.getVars().getPlayerRotationPitch();
		return shorten(Math.abs(rotationPitch) % 360.0) + "\u00b0";
	}

	@Override
	public String getTranslation() {
		return "ingame.pitch";
	}

}
