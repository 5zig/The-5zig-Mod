package eu.the5zig.mod.plugin;

import com.google.common.collect.Maps;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

public class PluginClassLoader extends URLClassLoader {

	private final Map<String, Class<?>> classes = Maps.newHashMap();
	private final PluginManagerImpl pluginManager;

	public PluginClassLoader(PluginManagerImpl pluginManager, ClassLoader parent, File file) throws MalformedURLException {
		super(new URL[]{file.toURI().toURL()}, parent);
		this.pluginManager = pluginManager;
	}

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return findClass(name, true);
	}

	Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
		Class<?> result = this.classes.get(name);
		if (result == null) {
			if (checkGlobal) {
				result = pluginManager.getClassByName(name, this);
			}

			if (result == null) {
				result = super.findClass(name);
			}

			this.classes.put(name, result);
		}
		return result;
	}

}
