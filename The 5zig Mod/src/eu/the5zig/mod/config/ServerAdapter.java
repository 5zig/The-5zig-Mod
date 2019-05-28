package eu.the5zig.mod.config;

import com.google.common.collect.Lists;
import com.google.gson.*;
import eu.the5zig.mod.server.Server;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ServerAdapter implements JsonSerializer<List<Server>>, JsonDeserializer<List<Server>> {

	private static final String CLASSNAME = "Name";
	private static final String VALUE = "Value";

	@Override
	public JsonElement serialize(List<Server> servers, Type type, JsonSerializationContext context) {
		JsonArray array = new JsonArray();
		for (Server server : servers) {
			JsonObject retValue = new JsonObject();
			String className = server.getClass().getCanonicalName();
			retValue.addProperty(CLASSNAME, className);
			JsonElement elem = context.serialize(server, server.getClass());
			retValue.add(VALUE, elem);
			array.add(retValue);
		}
		return array;
	}

	@Override
	public List<Server> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
		List<Server> servers = Lists.newArrayList();
		JsonArray array = jsonElement.getAsJsonArray();
		for (JsonElement element : array) {
			JsonObject jsonObject = element.getAsJsonObject();
			JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
			String className = prim.getAsString();
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
				if (!Server.class.isAssignableFrom(clazz))
					throw new IllegalArgumentException();
			} catch (Throwable ignored) {
				return servers;
			}
			servers.add(context.<Server>deserialize(jsonObject.get(VALUE), clazz));
		}
		return servers;
	}
}
