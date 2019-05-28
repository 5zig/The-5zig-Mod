package eu.the5zig.mod.event;

import java.lang.reflect.Method;

public class RegisteredEventHandler implements Comparable<RegisteredEventHandler> {

	private final Object instance;
	private final Method method;
	private final EventHandler eventHandler;

	public RegisteredEventHandler(Object instance, Method method, EventHandler eventHandler) {
		this.instance = instance;
		this.method = method;
		this.eventHandler = eventHandler;
	}

	public Object getInstance() {
		return instance;
	}

	public Method getMethod() {
		return method;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	@Override
	public int compareTo(RegisteredEventHandler o) {
		return o.eventHandler.priority().compareTo(eventHandler.priority());
	}
}
