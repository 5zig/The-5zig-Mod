package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatArena extends HypixelStatCategory {

	public HypixelStatArena() {
		super(HypixelGameType.ARENA);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseDouble(object, "rating"), parseInt(object, "kills_1v1"), parseInt(object, "deaths_1v1"), parseInt(object, "wins_1v1"),
				parseInt(object, "losses_1v1"), parseInt(object, "damage_1v1"), parseInt(object, "kills_2v2"), parseInt(object, "deaths_2v2"), parseInt(object, "wins_2v2"),
				parseInt(object, "losses_2v2"), parseInt(object, "damage_2v2"), parseInt(object, "kills_4v4"), parseInt(object, "deaths_4v4"), parseInt(object, "wins_4v4"),
				parseInt(object, "losses_4v4"), parseInt(object, "damage_4v4"));
	}
}
