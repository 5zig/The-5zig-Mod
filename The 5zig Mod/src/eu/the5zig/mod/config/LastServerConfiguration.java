package eu.the5zig.mod.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import eu.the5zig.mod.server.Server;

import java.io.File;
import java.util.List;

public class LastServerConfiguration extends Configuration<LastServer> {

	public LastServerConfiguration(File parent) {
		super(new File(parent, "lastServers.json"), LastServer.class);
	}

	@Override
	protected void registerTypeAdapters(GsonBuilder gsonBuilder) {
		gsonBuilder.registerTypeAdapter((new TypeToken<List<Server>>() {
		}).getType(), new ServerAdapter());
	}
}
