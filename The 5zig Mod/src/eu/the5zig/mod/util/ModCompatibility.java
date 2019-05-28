package eu.the5zig.mod.util;

import eu.the5zig.mod.asm.Transformer;

public class ModCompatibility {

	public static boolean hasReplayMod;

	static {
		try {
			if (Transformer.FORGE) {
				Class<?> loaderClass = Thread.currentThread().getContextClassLoader().loadClass("net.minecraftforge.fml.common.Loader");
				hasReplayMod = (Boolean) loaderClass.getMethod("isModLoaded", String.class).invoke(null, "replaymod");
			}
		} catch (Throwable ignored) {
		}
	}

}
