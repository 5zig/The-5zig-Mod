package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatWalls3 extends HypixelStatCategory {

	public HypixelStatWalls3() {
		super(HypixelGameType.WALLS3);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseInt(object, "coins"), parseInt(object, "kills"), parseInt(object, "deaths"), parseInt(object, "finalKills"), parseInt(object, "finalDeaths"),
				parseInt(object, "wins"), parseInt(object, "losses"), parseInt(object, "weeklyKills"), parseInt(object, "weeklyDeaths"), parseInt(object, "kills_Blaze"),
				parseInt(object, "deaths_Blaze"), parseInt(object, "finalKills_Blaze"), parseInt(object, "wins_Blaze"), parseInt(object, "losses_Blaze"), parseInt(object, "kills_Creeper"),
				parseInt(object, "deaths_Creeper"), parseInt(object, "finalKills_Creeper"), parseInt(object, "wins_Creeper"), parseInt(object, "losses_Creeper"),
				parseInt(object, "kills_Enderman"), parseInt(object, "deaths_Enderman"), parseInt(object, "finalKills_Enderman"), parseInt(object, "wins_Enderman"),
				parseInt(object, "losses_Enderman"), parseInt(object, "kills_Herobrine"), parseInt(object, "deaths_Herobrine"), parseInt(object, "finalKills_Herobrine"),
				parseInt(object, "wins_Herobrine"), parseInt(object, "losses_Herobrine"), parseInt(object, "kills_Skeleton"), parseInt(object, "deaths_Skeleton"),
				parseInt(object, "finalKills_Skeleton"), parseInt(object, "wins_Skeleton"), parseInt(object, "losses_Skeleton"), parseInt(object, "kills_Spider"),
				parseInt(object, "deaths_Spider"), parseInt(object, "finalKills_Spider"), parseInt(object, "wins_Spider"), parseInt(object, "losses_Spider"),
				parseInt(object, "kills_Zombie"), parseInt(object, "deaths_Zombie"), parseInt(object, "finalKills_Zombie"), parseInt(object, "wins_Zombie"),
				parseInt(object, "losses_Zombie"));
	}
}
