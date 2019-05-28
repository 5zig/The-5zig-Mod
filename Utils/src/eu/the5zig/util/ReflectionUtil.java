package eu.the5zig.util;

public class ReflectionUtil {

	public static Class<?> forName(String name) {
		try {
			return Class.forName(name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Class '" + name + "' not found!");
		}
	}

	public static <T> T cast(Class<T> c, Object o) {
		return c.cast(o);
	}

	public static Object fieldValue(Class c, Object o, String f) {
		try {
			return c.getField(f).get(o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object newInstance(Class<?> c, Class[] parameterTypes, Object[] args) {
		try {
			return c.getConstructor(parameterTypes).newInstance(args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Object invoke(Class<?> c, String m, Class[] parameterTypes, Object o, Object[] args) {
		try {
			return c.getMethod(m, parameterTypes).invoke(o, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
