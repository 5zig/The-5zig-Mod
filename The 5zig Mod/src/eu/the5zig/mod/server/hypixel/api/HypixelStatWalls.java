package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatWalls extends HypixelStatCategory {

	public HypixelStatWalls() {
		super(HypixelGameType.WALLS);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "kills"), parseInt(object, "deaths"), parseInt(object, "wins"), parseInt(object, "losses"),
				parseInt(object, "weekly_wins_a"), parseInt(object, "monthly_wins_b"));
	}
}
