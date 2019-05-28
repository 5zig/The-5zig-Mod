package eu.the5zig.mod.config.items;

import com.google.gson.JsonObject;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.gui.GuiSettings;

import java.util.Locale;

public class EnumItem<E extends Enum> extends ConfigItem<E> {

	private final Class<E> e;

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public EnumItem(String key, String category, E defaultValue, Class<E> e) {
		super(key, category, defaultValue);
		this.e = e;
	}

	@Override
	public void deserialize(JsonObject object) {
		//noinspection RedundantCast
		set((E) Enum.valueOf(e, object.get(getKey()).getAsString()));
	}

	@Override
	public void serialize(JsonObject object) {
		object.addProperty(getKey(), get().toString());
	}

	@Override
	public void next() {
		set(e.getEnumConstants()[(get().ordinal() + 1) % e.getEnumConstants().length]);
	}

	@Override
	public String translateValue() {
		return I18n.translate(getTranslationPrefix() + "." + getCategory() + "." + get().name().toLowerCase(Locale.ROOT));
	}
}