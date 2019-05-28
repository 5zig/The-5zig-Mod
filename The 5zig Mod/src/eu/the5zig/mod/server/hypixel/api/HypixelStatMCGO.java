package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatMCGO extends HypixelStatCategory {

	public HypixelStatMCGO() {
		super(HypixelGameType.MCGO);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "kills"), parseInt(object, "cop_kills"), parseInt(object, "criminal_kills"), parseInt(object, "deaths"),
				parseInt(object, "game_wins"), parseInt(object, "round_wins"), parseInt(object, "shots_fired"), parseInt(object, "bombs_planted"), parseInt(object, "bombs_defused"));
	}
}
