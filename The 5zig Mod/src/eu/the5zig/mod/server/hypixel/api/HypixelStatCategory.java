package eu.the5zig.mod.server.hypixel.api;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import eu.the5zig.mod.I18n;
import eu.the5zig.mod.server.hypixel.HypixelGameType;
import eu.the5zig.mod.util.JsonUtil;
import eu.the5zig.util.Utils;

import java.util.List;
import java.util.Locale;

public abstract class HypixelStatCategory {

	protected final HypixelGameType gameType;

	public HypixelStatCategory(HypixelGameType gameType) {
		this.gameType = gameType;
	}

	public HypixelGameType getGameType() {
		return gameType;
	}

	protected String translate(String name) {
		return I18n.translate("server.hypixel.stats." + getGameType().getDatabaseName().toLowerCase(Locale.ROOT) + "." + Utils.upperToDash(name));
	}

	protected String parseInt(JsonObject object, String name) {
		return translate(name) + ": " + JsonUtil.getInt(object, name);
	}

	protected String parseDouble(JsonObject object, String name) {
		return translate(name) + ": " + Utils.getShortenedDouble(JsonUtil.getDouble(object, name));
	}

	protected String parseLong(JsonObject object, String name) {
		return translate(name) + ": " + JsonUtil.getLong(object, name);
	}

	protected String parseTime(JsonObject object, String name) {
		return translate(name) + ": " + Utils.convertToDate(JsonUtil.getLong(object, name)).replace("Today",
				I18n.translate("profile.today").replace("Yesterday", I18n.translate("profile.yesterday")));
	}

	protected String parseDuration(JsonObject object, String name) {
		return translate(name) + ": " + Utils.convertToTime(JsonUtil.getLong(object, name));
	}

	protected String parseString(JsonObject object, String name) {
		String string = JsonUtil.getString(object, name);
		if (string == null)
			string = I18n.translate("server.hypixel.stats.none");
		return translate(name) + ": " + string;
	}

	protected List<String> parseList(JsonObject object, String name) {
		List<String> result = Lists.newArrayList();
		List<String> parse = JsonUtil.getList(object, name);
		for (String s : parse) {
			result.add(translate(name + "." + s));
		}
		return result;
	}

	public abstract List<String> getStats(JsonObject object);

}
