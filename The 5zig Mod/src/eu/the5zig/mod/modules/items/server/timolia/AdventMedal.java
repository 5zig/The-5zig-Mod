package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;
import eu.the5zig.util.minecraft.ChatColor;

public class AdventMedal extends GameModeItem<ServerTimolia.Advent> {

	public AdventMedal() {
		super(ServerTimolia.Advent.class, GameState.GAME, GameState.FINISHED);
	}

	@Override
	protected Object getValue(boolean dummy) {
		String medal = null;
		long medalTime = 0;
		if (dummy) {
			return "\u2726";
		}
		long currentTime = System.currentTimeMillis();
		long l = getGameMode().getState() == GameState.GAME ? currentTime - getGameMode().getTime() : getGameMode().getTime();
		if (l <= getGameMode().getTimeGold()) {
			medal = ChatColor.GOLD.toString();
			medalTime = getGameMode().getTimeGold();
		} else if (l <= getGameMode().getTimeSilver()) {
			medal = ChatColor.GRAY.toString();
			medalTime = getGameMode().getTimeSilver();
		} else if (l <= getGameMode().getTimeBronze()) {
			medal = ChatColor.DARK_AQUA.toString();
			medalTime = getGameMode().getTimeBronze();
		}
		if (medal != null) {
			int millis = (int) (medalTime % 1000);
			medalTime /= 1000;
			int seconds = (int) (medalTime % 60);
			medalTime /= 60;
			int minutes = (int) medalTime;
			medal += "\u2726 " + ChatColor.RESET + "(" + String.format("%d:%02d.%03d", minutes, seconds, millis) + ")";
			return medal;
		}
		return null;
	}

	@Override
	public String getTranslation() {
		return "ingame.medal";
	}
}
