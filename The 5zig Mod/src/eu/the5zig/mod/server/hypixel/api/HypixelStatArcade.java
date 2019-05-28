package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatArcade extends HypixelStatCategory {

	public HypixelStatArcade() {
		super(HypixelGameType.ARCADE);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "kills_dayone"), parseInt(object, "wins_dayone"), parseInt(object, "headshots_dayone"),
				parseInt(object, "kills_dragonwars2"), parseInt(object, "kills_oneinthequiver"), parseInt(object, "deaths_oneinthequiver"), parseInt(object, "wins_oneinthequiver"),
				parseInt(object, "bounty_kills_oneinthequiver"), parseInt(object, "kills_throw_out"), parseInt(object, "deaths_throw_out"), parseInt(object, "poop_collected"),
				parseInt(object, "wins_party"), parseInt(object, "wins_party2"), parseInt(object, "sw_kills"), parseInt(object, "sw_empire_kills"), parseInt(object, "sw_rebel_kills"),
				parseInt(object, "sw_deaths"), parseInt(object, "sw_shots_fired"), parseInt(object, "sw_weekly_kills_a"), parseInt(object, "sw_monthly_kills_a"),
				parseInt(object, "wins_farm_hunt"));
	}

}
