package eu.the5zig.mod.config.items;

import eu.the5zig.mod.I18n;

public class ToggleServerStatsItem extends BoolItem {

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link eu.the5zig.mod.gui.GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public ToggleServerStatsItem(String key, String category, Boolean defaultValue) {
		super(key, category, defaultValue);
	}

	@Override
	public String translate() {
		return I18n.translate("config.server_general.show_server_stats") + ": " + translateValue();
	}
}
