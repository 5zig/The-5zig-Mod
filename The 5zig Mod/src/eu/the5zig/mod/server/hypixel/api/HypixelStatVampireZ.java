package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatVampireZ extends HypixelStatCategory {

	public HypixelStatVampireZ() {
		super(HypixelGameType.VAMPIREZ);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "human_kills"), parseInt(object, "human_deaths"), parseInt(object, "human_wins"), parseInt(object, "vampire_kills"),
				parseInt(object, "vampire_deaths"), parseInt(object, "most_vampire_kills"), parseInt(object, "zombie_kills"));
	}
}
