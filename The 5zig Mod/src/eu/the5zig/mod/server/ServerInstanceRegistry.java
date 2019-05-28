package eu.the5zig.mod.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ServerInstanceRegistry {

	private final Map<String, RegisteredServerInstance> registeredServerInstances = Maps.newHashMap();

	public ServerInstanceRegistry() {
	}

	public void registerServerInstance(ServerInstance serverInstance) {
		if (registeredServerInstances.containsKey(serverInstance.getConfigName())) {
			throw new IllegalArgumentException("Server with config name " + serverInstance.getConfigName() + " already has been registered!");
		}
		registeredServerInstances.put(serverInstance.getConfigName(), new RegisteredServerInstance(serverInstance, Thread.currentThread().getContextClassLoader()));
	}

	public void registerServerInstance(Class<? extends ServerInstance> clazz, ClassLoader classLoader) {
		ServerInstance serverInstance;
		try {
			serverInstance = clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Could not create new server instance!", e);
		}
		if (registeredServerInstances.containsKey(serverInstance.getConfigName())) {
			throw new IllegalArgumentException("Server with config name " + serverInstance.getConfigName() + " already has been registered!");
		}
		registeredServerInstances.put(serverInstance.getConfigName(), new RegisteredServerInstance(serverInstance, classLoader));
	}

	public void unregisterServerInstance(Class<? extends ServerInstance> clazz) {
		String key = null;
		for (RegisteredServerInstance instance : registeredServerInstances.values()) {
			if (clazz.isAssignableFrom(instance.getServerInstance().getClass())) {
				key = instance.getServerInstance().getConfigName();
			}
		}
		if (key != null) {
			registeredServerInstances.remove(key);
		}
	}

	public RegisteredServerInstance getRegisteredInstance(String configName) {
		return registeredServerInstances.get(configName);
	}

	public ServerInstance byConfigName(String configName) {
		return registeredServerInstances.get(configName).getServerInstance();
	}

	public Collection<RegisteredServerInstance> getRegisteredInstances() {
		return registeredServerInstances.values();
	}

	public List<String> getServerNames() {
		return Lists.newArrayList(registeredServerInstances.keySet());
	}

}
