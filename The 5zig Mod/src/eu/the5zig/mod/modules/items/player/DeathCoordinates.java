package eu.the5zig.mod.modules.items.player;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.manager.DeathLocation;
import eu.the5zig.mod.manager.WorldType;
import eu.the5zig.mod.modules.StringItem;

public class DeathCoordinates extends StringItem {

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return shorten(10) + "/" + shorten(64) + "/" + shorten(10);
		}
		DeathLocation location = The5zigMod.getDataManager().getDeathLocation();
		String x = shorten(location.getCoordinates().getX());
		String y = shorten(location.getCoordinates().getY());
		String z = shorten(location.getCoordinates().getZ());
		String result = x + "/" + y + "/" + z;
		if (location.getWorldType() != WorldType.OVERWORLD) {
			result += "(" + I18n.translate("modules.item.death_coordinates." + location.getWorldType().toString().toLowerCase()) + ")";
		}
		return result;
	}

	@Override
	public boolean shouldRender(boolean dummy) {
		return dummy || The5zigMod.getDataManager().getDeathLocation() != null;
	}

	@Override
	public String getTranslation() {
		return "ingame.death_coordinates";
	}
}
