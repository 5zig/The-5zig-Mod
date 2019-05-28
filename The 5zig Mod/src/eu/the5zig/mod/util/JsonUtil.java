package eu.the5zig.mod.util;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

public class JsonUtil {

	private JsonUtil() {
	}

	public static int getInt(JsonObject object, String name) {
		JsonElement element = object.get(name);
		if (element == null || element.isJsonNull())
			return 0;
		return element.getAsInt();
	}

	public static double getDouble(JsonObject object, String name) {
		JsonElement element = object.get(name);
		if (element == null || element.isJsonNull())
			return 0;
		return element.getAsDouble();
	}

	public static long getLong(JsonObject object, String name) {
		JsonElement element = object.get(name);
		if (element == null || element.isJsonNull())
			return 0;
		return element.getAsLong();
	}

	public static String getString(JsonObject object, String name) {
		JsonElement element = object.get(name);
		if (element == null || element.isJsonNull())
			return null;
		return element.getAsString();
	}

	public static List<String> getList(JsonObject object, String name) {
		List<String> result = Lists.newArrayList();
		JsonElement element = object.get(name);
		if (element == null || element.isJsonNull())
			return result;
		for (JsonElement jsonElement : element.getAsJsonArray()) {
			result.add(jsonElement.getAsString());
		}
		return result;
	}

}
