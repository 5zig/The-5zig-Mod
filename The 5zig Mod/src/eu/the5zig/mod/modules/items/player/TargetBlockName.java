package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;
import eu.the5zig.mod.util.IResourceLocation;

public class TargetBlockName extends StringItem {

	private static IResourceLocation DUMMY_BLOCK = The5zigMod.getVars().createResourceLocation("minecraft", "stone");

	@Override
	public void registerSettings() {
		getProperties().addSetting("showResourceDomain", true);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (!dummy && !The5zigMod.getVars().hasTargetBlock()) {
			return null;
		}
		IResourceLocation resourceLocation = dummy ? DUMMY_BLOCK : The5zigMod.getVars().getTargetBlockName();
		if ((Boolean) getProperties().getSetting("showResourceDomain").get()) {
			return resourceLocation.getResourceDomain() + ":" + resourceLocation.getResourcePath();
		} else {
			return resourceLocation.getResourcePath();
		}
	}

	@Override
	public String getTranslation() {
		return "ingame.target_name";
	}
}
