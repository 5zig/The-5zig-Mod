package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;
import eu.the5zig.mod.render.RenderLocation;

public class Speed extends StringItem {

	@Override
	public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
		The5zigMod.getDataManager().getSpeedCalculator().update();
		super.render(x, y, renderLocation, dummy);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return (dummy ? shorten(2.3) : shorten(The5zigMod.getDataManager().getSpeedCalculator().getCurrentSpeed())) + " m/s";
	}

	@Override
	public String getTranslation() {
		return "ingame.speed";
	}
}
