package eu.the5zig.mod.config.items;

import com.google.gson.JsonObject;
import eu.the5zig.mod.gui.GuiSettings;

public class IntItem extends ConfigItem<Integer> {

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public IntItem(String key, String category, Integer defaultValue) {
		super(key, category, defaultValue);
	}

	@Override
	public void deserialize(JsonObject object) {
		set(object.get(getKey()).getAsInt());
	}

	@Override
	public void serialize(JsonObject object) {
		object.addProperty(getKey(), get());
	}

	@Override
	public void next() {
		set(get() + 1);
	}
}