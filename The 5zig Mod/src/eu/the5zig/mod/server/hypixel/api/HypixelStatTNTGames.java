package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatTNTGames extends HypixelStatCategory {

	public HypixelStatTNTGames() {
		super(HypixelGameType.TNTGAMES);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "wins_tntrun"), parseInt(object, "wins_tntag"), parseInt(object, "tags_bowspleef"),
				parseInt(object, "deaths_bowspleef"), parseInt(object, "wins_bowspleef"), parseInt(object, "kills_capture"), parseInt(object, "deaths_capture"),
				parseInt(object, "wins_capture"));
	}
}
