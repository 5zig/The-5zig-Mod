package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.ingame.ItemStack;
import eu.the5zig.mod.modules.items.ItemStackItem;

public class Soups extends ItemStackItem {

	@Override
	protected ItemStack getStack(boolean dummy) {
		if (dummy) {
			return The5zigMod.getVars().getItemByName("minecraft:mushroom_stew");
		}
		int soupCount = The5zigMod.getVars().getItemCount("minecraft:mushroom_stew");
		if (soupCount > 0) {
			return The5zigMod.getVars().getItemByName("minecraft:mushroom_stew", soupCount);
		}
		return null;
	}
}
