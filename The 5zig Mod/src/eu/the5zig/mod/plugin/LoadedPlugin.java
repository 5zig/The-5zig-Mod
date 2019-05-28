package eu.the5zig.mod.plugin;

import com.google.common.collect.Lists;
import eu.the5zig.mod.modules.AbstractModuleItem;
import eu.the5zig.mod.server.ServerInstance;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LoadedPlugin {

	private final String name;
	private final String version;
	private final Object instance;
	private final PluginClassLoader classLoader;
	private final Map<Locale, ResourceBundle> locales;
	private final List<Method> loadMethods;
	private final List<Method> unloadMethods;
	private final File file;

	private final List<Object> registeredListeners = Lists.newArrayList();
	private final List<Class<? extends AbstractModuleItem>> registeredModuleItems = Lists.newArrayList();
	private final List<Class<? extends ServerInstance>> registeredServerInstances = Lists.newArrayList();

	public LoadedPlugin(String name, String version, Object instance, PluginClassLoader classLoader, Map<Locale, ResourceBundle> locales, List<Method> loadMethods, List<Method> unloadMethods, File file) {
		this.name = name;
		this.version = version;
		this.instance = instance;
		this.classLoader = classLoader;
		this.locales = locales;
		this.loadMethods = loadMethods;
		this.unloadMethods = unloadMethods;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public Object getInstance() {
		return instance;
	}

	public PluginClassLoader getClassLoader() {
		return classLoader;
	}

	public Map<Locale, ResourceBundle> getLocales() {
		return locales;
	}

	public List<Method> getLoadMethods() {
		return loadMethods;
	}

	public List<Method> getUnloadMethods() {
		return unloadMethods;
	}

	public File getFile() {
		return file;
	}

	public List<Object> getRegisteredListeners() {
		return registeredListeners;
	}

	public List<Class<? extends AbstractModuleItem>> getRegisteredModuleItems() {
		return registeredModuleItems;
	}

	public List<Class<? extends ServerInstance>> getRegisteredServerInstances() {
		return registeredServerInstances;
	}
}
