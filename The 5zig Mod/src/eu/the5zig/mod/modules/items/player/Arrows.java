package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.ingame.ItemStack;
import eu.the5zig.mod.modules.items.ItemStackItem;

public class Arrows extends ItemStackItem {

	private static final String[] ARROW_TYPES = {"arrow", "spectral_arrow", "tipped_arrow"};

	@Override
	public void registerSettings() {
		getProperties().addSetting("showAlways", false);
	}

	@Override
	protected ItemStack getStack(boolean dummy) {
		if (dummy) {
			return The5zigMod.getVars().getItemByName("minecraft:arrow", 10);
		}
		int arrowCount = 0;
		for (String arrowType : ARROW_TYPES) {
			arrowCount += The5zigMod.getVars().getItemCount("minecraft:" + arrowType);
		}
		ItemStack mainHand = The5zigMod.getVars().getItemInMainHand();
		ItemStack offHand = The5zigMod.getVars().getItemInOffHand();
		if (arrowCount > 0) {
			if ((Boolean) getProperties().getSetting("showAlways").get() || (mainHand != null && "minecraft:bow".equals(mainHand.getKey()))) {
				return The5zigMod.getVars().getItemByName("minecraft:arrow", arrowCount);
			}
			if ((Boolean) getProperties().getSetting("showAlways").get() || (offHand != null && "minecraft:bow".equals(offHand.getKey()))) {
				return The5zigMod.getVars().getItemByName("minecraft:arrow", arrowCount);
			}
		}
		return null;
	}
}
