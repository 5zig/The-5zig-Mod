package eu.the5zig.mod.server.venicraft;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VenicraftListener extends AbstractGameListener<GameMode> {

	private static final Pattern GAMEMODE_PATTERN = Pattern.compile("\\sDu spielst auf einem .+ Server \u00bb (.+)\\.\\s");

	@Override
	public Class<GameMode> getGameMode() {
		return null;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return false;
	}

	@Override
	public void onPlayerListHeaderFooter(GameMode gameMode, String header, String footer) {
		//   §a◀ §5§lVeniCraft §8» §7Servernetzwerk§f §a▶
		if (!header.equals(
				"  " + ChatColor.GREEN + "\u25c0 " + ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "VeniCraft " + ChatColor.DARK_GRAY + "\u00bb " + ChatColor.GRAY + "Servernetzwerk" +
						ChatColor.WHITE + " " + ChatColor.GREEN + "\u25b6   "))
			return;
		//   §7Unser §6§lPremium §7Shop
		//   §a» §3shop§8.§3Venicraft§8.§3at §a«
		//
		//  §3Du spielst auf einem §bCrystalDefence §3Server §3» §bcd2.
		Matcher matcher = GAMEMODE_PATTERN.matcher(ChatColor.stripColor(footer));
		if (matcher.matches()) {
			String lobby = matcher.group(1);
			getGameListener().switchLobby(lobby);
		} else {
			getGameListener().switchLobby("");
		}
	}

	/*@Override
	public void onTick(GameMode gameMode) {
		if (getGameListener().getCurrentGameMode() == null) {
			ScoreboardImpl scoreboard = The5zigMod.getVars().getScoreboard();
			if (scoreboard != null) {
				String lobby = scoreboard.getLines().get(3);
				if (lobby != null) {
					getGameListener().switchLobby(ChatColor.stripColor(lobby));
				}
			}
		}
	}*/

}
