package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.manager.AFKManager;
import eu.the5zig.mod.modules.StringItem;
import eu.the5zig.util.Utils;
import eu.the5zig.util.minecraft.ChatColor;

public class AFKTime extends StringItem {
	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return Utils.convertToClock(1000 * 60);
		}
		if (The5zigMod.getDataManager().getAfkManager().getAFKTime() > AFKManager.AFK_COUNTER) {
			return Utils.convertToClock(The5zigMod.getDataManager().getAfkManager().getAFKTime());
		}
		if (The5zigMod.getDataManager().getAfkManager().getLastAfkTime() != 0) {
			return ChatColor.UNDERLINE + Utils.convertToClock(The5zigMod.getDataManager().getAfkManager().getLastAfkTime());
		}
		return null;
	}

	@Override
	public String getTranslation() {
		return "ingame.afk";
	}
}
