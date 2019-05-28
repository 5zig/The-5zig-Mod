package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatUHC extends HypixelStatCategory {

	public HypixelStatUHC() {
		super(HypixelGameType.UHC);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "kills"), parseInt(object, "deaths"), parseInt(object, "wins"), parseInt(object, "score"),
				parseInt(object, "heads_eaten"));
	}
}
