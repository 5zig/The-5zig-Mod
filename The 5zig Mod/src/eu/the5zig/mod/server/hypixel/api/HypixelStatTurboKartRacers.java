package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatTurboKartRacers extends HypixelStatCategory {

	public HypixelStatTurboKartRacers() {
		super(HypixelGameType.TURBO_KART_RACERS);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "wins"), parseInt(object, "gold_trophy"), parseInt(object, "silver_trophy"), parseInt(object, "bronze_trophy"),
				parseInt(object, "laps_completed"), parseInt(object, "coins_picked_up"), parseInt(object, "box_pickups"), parseInt(object, "banana_hits_sent"),
				parseInt(object, "banana_hits_received"));
	}
}
