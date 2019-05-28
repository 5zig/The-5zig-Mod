package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatSkyWars extends HypixelStatCategory {

	public HypixelStatSkyWars() {
		super(HypixelGameType.SKYWARS);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "kills"), parseInt(object, "assists"), parseInt(object, "deaths"), parseInt(object, "wins"),
				parseInt(object, "win_streak"), parseInt(object, "losses"), parseInt(object, "blocks_placed"), parseInt(object, "blocks_broken"), parseInt(object, "quits"),
				parseInt(object, "soul_well"), parseInt(object, "soul_well_legendaries"), parseInt(object, "soul_well_rares"), parseInt(object, "souls_gathered"),
				parseInt(object, "egg_thrown"), parseInt(object, "enderpearls_thrown"), parseInt(object, "arrows_shot"), parseInt(object, "arrows_hit"), parseInt(object, "items_enchanted"));
	}
}
