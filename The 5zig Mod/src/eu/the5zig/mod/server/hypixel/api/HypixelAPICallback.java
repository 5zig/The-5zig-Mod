package eu.the5zig.mod.server.hypixel.api;

import eu.the5zig.mod.The5zigMod;

public abstract class HypixelAPICallback {

	public abstract void call(HypixelAPIResponse response);

	public void call(HypixelAPIResponseException exception) {
		The5zigMod.logger.warn("An unhandled exception occurred!", exception);
	}

}
