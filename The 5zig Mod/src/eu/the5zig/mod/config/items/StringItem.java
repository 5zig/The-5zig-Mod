package eu.the5zig.mod.config.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import eu.the5zig.mod.gui.GuiSettings;

public class StringItem extends ConfigItem<String> {

	private int minLength;
	private int maxLength;

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public StringItem(String key, String category, String defaultValue) {
		this(key, category, defaultValue, 0, 255);
	}

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public StringItem(String key, String category, String defaultValue, int minLength, int maxLength) {
		super(key, category, defaultValue);
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	@Override
	public void deserialize(JsonObject object) {
		JsonElement element = object.get(getKey());
		set(element.isJsonNull() ? null : element.getAsString());
	}

	@Override
	public void serialize(JsonObject object) {
		object.addProperty(getKey(), get());
	}

	@Override
	public void next() {
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
}
