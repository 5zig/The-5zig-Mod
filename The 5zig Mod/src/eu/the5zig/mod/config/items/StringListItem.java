package eu.the5zig.mod.config.items;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public class StringListItem extends ListItem<String> {

	/**
	 * Creates a Config Item.
	 *
	 * @param key          Der Key of the Item. Used in config File and to translate the Item.
	 * @param category     The Category of the Item. Used by {@link eu.the5zig.mod.gui.GuiSettings} for finding the corresponding items.
	 * @param defaultValue The Default Value of the Item.
	 */
	public StringListItem(String key, String category, List<String> defaultValue) {
		super(key, category, defaultValue);
	}

	@Override
	public void deserialize(JsonObject object) {
		List<String> list = Lists.newArrayList();
		JsonArray array = object.getAsJsonArray(getKey());
		for (JsonElement jsonElement : array) {
			if (jsonElement.isJsonPrimitive()) {
				list.add(jsonElement.getAsString());
			}
		}
		set(list);
	}

	@Override
	public void serialize(JsonObject object) {
		if (get().isEmpty()) {
			object.remove(getKey());
		} else {
			JsonArray array = new JsonArray();
			for (String element : get()) {
				array.add(new JsonPrimitive(element));
			}
			object.add(getKey(), array);
		}
	}

	@Override
	public void setSafely(List<Object> objects) {
		List<String> entries = new ArrayList<String>(objects.size());
		for (Object object : objects) {
			entries.add(String.valueOf(object));
		}
		set(entries);
	}
}
