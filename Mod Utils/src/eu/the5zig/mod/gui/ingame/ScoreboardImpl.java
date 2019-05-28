package eu.the5zig.mod.gui.ingame;

import java.util.HashMap;

public class ScoreboardImpl implements Scoreboard {

	private final String title;
	private final HashMap<String, Integer> lines;

	public ScoreboardImpl(String title, HashMap<String, Integer> lines) {
		this.title = title;
		this.lines = lines;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public HashMap<String, Integer> getLines() {
		return lines;
	}
}
