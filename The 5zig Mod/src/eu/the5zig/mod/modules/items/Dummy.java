package eu.the5zig.mod.modules.items;

import eu.the5zig.mod.modules.StringItem;

public class Dummy extends StringItem {

	@Override
	public void registerSettings() {
		getProperties().addSetting("dummy", true);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return getProperties().getSetting("dummy").get();
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		return getValue(dummy) != null && !dummy;
	}

	@Override
	public String getName() {
		return "Dummy";
	}
}
