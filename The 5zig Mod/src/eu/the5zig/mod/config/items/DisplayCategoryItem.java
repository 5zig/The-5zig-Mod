package eu.the5zig.mod.config.items;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.GuiSettings;

public class DisplayCategoryItem extends NonConfigItem {

	private String displayCategory;

	/**
	 * Creates an Item that is able to display a new Category.
	 *
	 * @param key             Der Key of the Item. Used in config File and to translate the Item.
	 * @param category        The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param displayCategory The Category that should be displayed next.
	 */
	public DisplayCategoryItem(String key, String category, String displayCategory) {
		super(key, category);
		this.displayCategory = displayCategory;
	}

	@Override
	public void action() {
		The5zigMod.getVars().displayScreen(new GuiSettings(The5zigMod.getVars().getCurrentScreen(), displayCategory));
	}
}
