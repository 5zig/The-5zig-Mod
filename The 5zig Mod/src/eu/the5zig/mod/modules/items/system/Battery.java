package eu.the5zig.mod.modules.items.system;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.StringItem;
import eu.the5zig.mod.util.ISystemPowerStatus;
import eu.the5zig.util.Utils;

public class Battery extends StringItem {

	@Override
	public void registerSettings() {
		getProperties().addSetting("chargeStatus", false);
	}

	@Override
	protected Object getValue(boolean dummy) {
		ISystemPowerStatus batteryStatus = The5zigMod.getVars().getBatteryStatus();
		if (batteryStatus == null) {
			return "100%";
		}

		String string = batteryStatus.getBatteryPercentage() + "%";
		if ((Boolean) getProperties().getSetting("chargeStatus").get()) {
			string += " (" + (batteryStatus.isPluggedIn() ? I18n.translate("modules.item.battery.plugged_in") : I18n.translate("modules.item.battery.un_plugged")) +
					(batteryStatus.getRemainingLifeTime() > 0 ? ": " + Utils.convertToTime(batteryStatus.getRemainingLifeTime() * 1000, false) : "") + ")";
		}

		return string;
	}

	@Override
	public String getTranslation() {
		return "ingame.battery";
	}
}
