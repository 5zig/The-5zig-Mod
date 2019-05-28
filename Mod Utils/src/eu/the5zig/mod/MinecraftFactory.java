package eu.the5zig.mod;

import eu.the5zig.mod.util.ClassProxyCallback;
import eu.the5zig.mod.util.IVariables;

public class MinecraftFactory {

	private static final IVariables variables;
	private static ClassProxyCallback classProxyCallback;

	static {
		try {
			variables = (IVariables) Class.forName("Variables").newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static IVariables getVars() {
		return variables;
	}

	public static void setClassProxyCallback(ClassProxyCallback classProxyCallback) {
		MinecraftFactory.classProxyCallback = classProxyCallback;
	}

	public static ClassProxyCallback getClassProxyCallback() {
		return classProxyCallback;
	}

}
