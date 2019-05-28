package eu.the5zig.mod.api;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class ServerAPIBackend {

	private String displayName = "Unknown Server";
	private final LinkedHashMap<String, String> stats = Maps.newLinkedHashMap();
	private HashMap<Integer, String> imageCache = Maps.newHashMap();
	private String base64;
	private String largeText;
	private long countdownTime;
	private String countdownName;
	private String lobby;

	public ServerAPIBackend() {
	}

	public void updateStat(String name, String score) {
		stats.put(name, score);
	}

	public void resetStat(String stat) {
		stats.remove(stat);
	}

	public void reset() {
		stats.clear();
		imageCache.clear();
		displayName = "Unknown Server";
		largeText = null;
		base64 = null;
		resetCountdown();
		lobby = null;
	}

	public Map<String, String> getStats() {
		return stats;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setImage(String base64, int id) {
		this.base64 = base64;
		imageCache.put(id, base64);
	}

	public void setImage(int id) {
		this.base64 = imageCache.get(id);
	}

	public void resetImage() {
		this.base64 = null;
	}

	public String getBase64() {
		return base64;
	}

	public void setLargeText(String largeText) {
		this.largeText = largeText;
	}

	public String getLargeText() {
		return largeText;
	}

	public void startCountdown(String name, long time) {
		countdownName = name;
		countdownTime = System.currentTimeMillis() + time;
	}

	public void resetCountdown() {
		countdownTime = -1;
		countdownName = null;
	}

	public long getCountdownTime() {
		if (countdownTime - System.currentTimeMillis() < 0)
			resetCountdown();
		return countdownTime;
	}

	public String getCountdownName() {
		return countdownName;
	}

	public String getLobby() {
		return lobby;
	}

	public void setLobby(String lobby) {
		this.lobby = lobby;
	}
}
