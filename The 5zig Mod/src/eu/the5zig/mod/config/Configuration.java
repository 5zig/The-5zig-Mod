package eu.the5zig.mod.config;

import com.google.common.base.Charsets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.Server;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration<T> {

	protected final File configFile;
	protected final Class<T> configClass;
	protected T configInstance;

	public Configuration(File configFile, Class<T> configClass) {
		this.configFile = configFile;
		this.configClass = configClass;
		setupWorkspace();
	}

	protected void setupWorkspace() {
		try {
			this.configInstance = loadConfig();
			saveConfig();
		} catch (Exception e) {
			The5zigMod.logger.fatal("Could not load Configurations!", e);
		}
	}

	public T createNewConfig(Class<T> configuration, File file) throws IOException {
		if (file.exists()) {
			if (!file.delete())
				throw new IOException("Could not delete Config File at " + file.getPath());
		}
		if (!file.createNewFile())
			throw new IOException("Could not create new Config File at " + file.getPath());

		T instance;
		try {
			instance = configuration.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Could not create a new instance of " + configuration.getName(), e);
		}

		try {
			saveConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		The5zigMod.logger.info("Created new Configuration at " + file.getPath());
		return instance;
	}

	public void saveConfig() {
		The5zigMod.getAsyncExecutor().execute(new Runnable() {
			@Override
			public void run() {
				try {
					GsonBuilder gsonBuilder = new GsonBuilder();
					registerTypeAdapters(gsonBuilder);
					Gson gson = gsonBuilder.setPrettyPrinting().create();
					String json = gson.toJson(configInstance);
					FileUtils.writeStringToFile(configFile, json, Charsets.UTF_8);
				} catch (IOException e) {
					The5zigMod.logger.warn("Could not update Config File!", e);
				}
			}
		});
	}

	protected void registerTypeAdapters(GsonBuilder gsonBuilder) {}

	private T loadConfig() throws IOException {
		if (!configFile.exists()) {
			return createNewConfig(configClass, configFile);
		}
		String reader = FileUtils.readFileToString(configFile, Charsets.UTF_8);
		GsonBuilder gsonBuilder = new GsonBuilder();
		if (LastServer.class.isAssignableFrom(configClass))
			gsonBuilder.registerTypeAdapter((new TypeToken<List<Server>>() {
			}).getType(), new ServerAdapter());
		try {
			T t = gsonBuilder.create().fromJson(reader, configClass);
			return t == null ? createNewConfig(configClass, configFile) : t;
		} catch (Exception e) {
			The5zigMod.logger.warn("Could not parse JSON at " + configFile.getPath(), e);
			return createNewConfig(configClass, configFile);
		}
	}

	public T getConfigInstance() {
		return configInstance;
	}
}
