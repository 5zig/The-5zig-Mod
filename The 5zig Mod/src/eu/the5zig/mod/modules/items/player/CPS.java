package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;
import eu.the5zig.mod.render.RenderLocation;

public class CPS extends StringItem {

	@Override
	public void registerSettings() {
		getProperties().addSetting("type", Type.SUM, Type.class);
	}

	@Override
	public void render(int x, int y, RenderLocation renderLocation, boolean dummy) {
		The5zigMod.getDataManager().getCpsManager().getLeftClickCounter().update();
		The5zigMod.getDataManager().getCpsManager().getRightClickCounter().update();
		super.render(x, y, renderLocation, dummy);
	}

	@Override
	protected Object getValue(boolean dummy) {
		int leftClicks = dummy ? 8 : (int) The5zigMod.getDataManager().getCpsManager().getLeftClickCounter().getCurrentCount();
		int rightClicks = dummy ? 3 : (int) The5zigMod.getDataManager().getCpsManager().getRightClickCounter().getCurrentCount();
		switch ((Type) getProperties().getSetting("type").get()) {
			case LEFT_CLICK:
				return leftClicks;
			case RIGHT_CLICK:
				return rightClicks;
			case SUM:
				return leftClicks + rightClicks;
			case BOTH:
				return leftClicks + " | " + rightClicks;
			default:
				return null;
		}
	}

	@Override
	public String getTranslation() {
		return "ingame.cps";
	}

	private enum Type {
		LEFT_CLICK, RIGHT_CLICK, SUM, BOTH
	}
}
