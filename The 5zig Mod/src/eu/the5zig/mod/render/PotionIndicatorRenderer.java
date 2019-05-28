package eu.the5zig.mod.render;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.ingame.PotionEffect;
import eu.the5zig.mod.util.GLUtil;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class PotionIndicatorRenderer {

	public void render() {
		if (!The5zigMod.getConfig().getBool("showPotionIndicator") || (The5zigMod.getDataManager().getServer() != null && !The5zigMod.getDataManager().getServer().isRenderPotionIndicator()))
			return;
		PotionEffect potionEffect = The5zigMod.getVars().getPotionForVignette();
		if (potionEffect == null)
			return;
		if (potionEffect.isGood()) {
			int maxTime = 20 * 60;
			double percent = (double) potionEffect.getTime() / (double) maxTime;
			float intensity = (float) (0.3 + percent);
			GLUtil.color(intensity, 0, intensity, 1);
		} else {
			int maxTime = 20 * 60;
			double percent = (double) potionEffect.getTime() / (double) maxTime;
			float intensity = (float) (0.2 + percent);
			GLUtil.color(0, intensity, intensity, 1);
		}
	}

}
