package eu.the5zig.mod.config.items;

import eu.the5zig.mod.I18n;
import eu.the5zig.util.Utils;

import java.util.List;

public abstract class ListItem<T> extends ConfigItem<List<T>> {

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link eu.the5zig.mod.gui.GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public ListItem(String key, String category, List<T> defaultValue) {
		super(key, category, defaultValue);
	}

	public abstract void setSafely(List<Object> objects);

	@Override
	public String translate() {
		return I18n.translate(getTranslationPrefix() + "." + getCategory() + "." + Utils.upperToDash(getKey()));
	}

	@Override
	public void next() {
	}
}
