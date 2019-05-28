package eu.the5zig.mod.config.items;

import com.google.gson.JsonObject;
import eu.the5zig.mod.gui.GuiSettings;
import eu.the5zig.util.Utils;

public class FloatItem extends ConfigItem<Float> {

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public FloatItem(String key, String category, Float defaultValue) {
		super(key, category, defaultValue);
	}

	@Override
	public void deserialize(JsonObject object) {
		set(object.get(getKey()).getAsFloat());
	}

	@Override
	public void serialize(JsonObject object) {
		object.addProperty(getKey(), get());
	}

	@Override
	public void next() {
		set(get() + 1);
	}

	@Override
	public String translateValue() {
		return String.valueOf(Utils.getShortenedFloat(get(), 1));
	}
}