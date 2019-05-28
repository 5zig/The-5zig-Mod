package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatBattleground extends HypixelStatCategory {

	public HypixelStatBattleground() {
		super(HypixelGameType.BATTLEGROUND);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseString(object, "chosen_class"), parseInt(object, "kills"), parseInt(object, "assists"), parseInt(object, "deaths"),
				parseInt(object, "wins"), parseInt(object, "wins_domination"), parseInt(object, "wins_capturetheflag"), parseInt(object, "losses"), parseInt(object, "damage"),
				parseInt(object, "repaired"), parseInt(object, "flag_conquer_self"));
	}
}
