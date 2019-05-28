package eu.the5zig.mod.config.items;

import eu.the5zig.mod.gui.GuiSettings;

public class PlaceholderItem extends NonConfigItem {

	private static int id = 0;

	/**
	 * Creates an invisible placeholder Item.
	 *
	 * @param category The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 */
	public PlaceholderItem(String category) {
		super("placeholder__" + id++, category);
	}

	@Override
	public final void action() {
	}
}
