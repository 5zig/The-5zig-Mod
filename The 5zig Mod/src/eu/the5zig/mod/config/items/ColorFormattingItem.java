package eu.the5zig.mod.config.items;

import com.google.gson.JsonObject;
import eu.the5zig.mod.gui.GuiSettings;
import eu.the5zig.util.minecraft.ChatColor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ColorFormattingItem extends ConfigItem<ChatColor> {

	private static final List<ChatColor> formattings = Arrays.asList(ChatColor.RESET, ChatColor.BOLD, ChatColor.ITALIC, ChatColor.UNDERLINE);

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public ColorFormattingItem(String key, String category, ChatColor defaultValue) {
		super(key, category, defaultValue);
	}

	@Override
	public void deserialize(JsonObject object) {
		set(ChatColor.valueOf(object.get(getKey()).getAsString()));
	}

	@Override
	public void serialize(JsonObject object) {
		object.addProperty(getKey(), get().name());
	}

	@Override
	public void next() {
		ChatColor current = !formattings.contains(get()) ? ChatColor.RESET : get();
		set(formattings.get((formattings.indexOf(current) + 1) % formattings.size()));
	}

	@Override
	public String translateValue() {
		return StringUtils.capitalize(get().getName().replace("_", ""));
	}
}
