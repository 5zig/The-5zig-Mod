package eu.the5zig.mod.modules.items.system;

import eu.the5zig.mod.modules.StringItem;
import eu.the5zig.util.Utils;

public class Memory extends StringItem {

	@Override
	public void registerSettings() {
		getProperties().addSetting("style", Style.PERCENTAGE, Style.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		long maxMemory = dummy ? (long) Math.pow(1024, 3) : Runtime.getRuntime().maxMemory();
		long totalMemory = dummy ? (long) Math.pow(1024, 3) : Runtime.getRuntime().totalMemory();
		long freeMemory = dummy ? (long) Math.pow(1024, 3) / 2 : Runtime.getRuntime().freeMemory();
		long usedMemory = totalMemory - freeMemory;
		double percentageUsed = ((double) usedMemory / (double) maxMemory) * 100.0;
		Style style = (Style) getProperties().getSetting("style").get();

		if (style == Style.PERCENTAGE) {
			return shorten(percentageUsed) + "%";
		} else if (style == Style.BYTES) {
			return Utils.bytesToReadable(usedMemory) + "/" + Utils.bytesToReadable(maxMemory);
		} else {
			return shorten(percentageUsed) + "% (" + Utils.bytesToReadable(usedMemory) + "/" + Utils.bytesToReadable(maxMemory) + ")";
		}
	}

	@Override
	public String getTranslation() {
		return "ingame.memory";
	}

	public enum Style {

		PERCENTAGE, BYTES, BOTH

	}
}
