package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatSurvivalGames extends HypixelStatCategory {

	public HypixelStatSurvivalGames() {
		super(HypixelGameType.SURVIVAL_GAMES);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "kills"), parseInt(object, "deaths"), parseInt(object, "wins"), parseInt(object, "wins_teams"),
				parseInt(object, "monthly_kills_b"));
	}
}
