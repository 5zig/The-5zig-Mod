package eu.the5zig.mod.server.hypixel.api;

import com.google.gson.JsonObject;
import eu.the5zig.mod.server.hypixel.HypixelGameType;

import java.util.Arrays;
import java.util.List;

public class HypixelStatGeneral extends HypixelStatCategory {

	public HypixelStatGeneral() {
		super(HypixelGameType.GENERAL);
	}

	@Override
	public List<String> getStats(JsonObject object) {
		return Arrays.asList(parseString(object, "displayname"), parseInt(object, "networkExp"), parseInt(object, "networkLevel"),
				parseInt(object, "karma"), parseInt(object, "vanityTokens"), parseInt(object, "tipsSent"), parseInt(object, "thanksSent"), parseTime(object, "firstLogin"),
				parseTime(object, "lastLogin"));
	}

}
