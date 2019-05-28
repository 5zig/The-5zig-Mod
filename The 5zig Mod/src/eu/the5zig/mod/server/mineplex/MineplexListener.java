package eu.the5zig.mod.server.mineplex;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;

public class MineplexListener extends AbstractGameListener<GameMode> {

	@Override
	public Class<GameMode> getGameMode() {
		return null;
	}

	@Override
	public boolean matchLobby(String lobby) {
		return false;
	}

	@Override
	public void onMatch(GameMode gameMode, String key, IPatternResult match) {
		if (key.equals("lobby")) {
			String lobby = match.get(0);
			getGameListener().switchLobby(lobby);
		}
	}

	@Override
	public void onPlayerListHeaderFooter(GameMode gameMode, String header, String footer) {
		// §lMineplex Network   §aLobby-22
		// Visit §awww.mineplex.com for News, Forums and Shop
		if (header.startsWith(ChatColor.BOLD + "Mineplex Network   " + ChatColor.GREEN + "Lobby-") || header.startsWith(ChatColor.GOLD.toString() + ChatColor.BOLD.toString())) {
			getGameListener().sendAndIgnore("/server", "lobby");
			String gameType = header.split(ChatColor.GREEN.toString() + "|" + ChatColor.GOLD.toString() + ChatColor.BOLD.toString())[1];
			getGameListener().switchLobby(gameType);
		}
	}

}
