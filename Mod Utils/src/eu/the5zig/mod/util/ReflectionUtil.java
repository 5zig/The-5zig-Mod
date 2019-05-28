package eu.the5zig.mod.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionUtil {

	private ReflectionUtil() {
	}

	public static Object invoke(Method method, Object... args) {
		return invoke(null, method, args);
	}

	public static Object invoke(Object instance, Method method, Object... args) {
		try {
			return method.invoke(instance, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T newInstance(Constructor<T> constructor, Object... args) {
		try {
			return constructor.newInstance(args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
