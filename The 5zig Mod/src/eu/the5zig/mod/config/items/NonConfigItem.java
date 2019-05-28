package eu.the5zig.mod.config.items;

import com.google.gson.JsonObject;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.gui.GuiSettings;
import eu.the5zig.util.Utils;

public abstract class NonConfigItem extends ConfigItem<Object> implements INonConfigItem {

	/**
	 * Creates an Item that cannot be serialized or deserialized.
	 *
	 * @param key      Der Key of the Item. Used in config File and to translate the Item.
	 * @param category The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 */
	public NonConfigItem(String key, String category) {
		super(key, category, null);
	}

	@Override
	public final void deserialize(JsonObject object) {
	}

	@Override
	public final void serialize(JsonObject object) {
	}

	@Override
	public final void next() {
	}

	@Override
	public abstract void action();

	@Override
	public String translate() {
		return I18n.translate(getTranslationPrefix() + "." + getCategory() + "." + Utils.upperToDash(getKey()));
	}

}
