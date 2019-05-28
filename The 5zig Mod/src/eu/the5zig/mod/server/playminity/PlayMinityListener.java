package eu.the5zig.mod.server.playminity;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.IPatternResult;
import eu.the5zig.util.minecraft.ChatColor;

public class PlayMinityListener extends AbstractGameListener<GameMode> {

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
		footer = ChatColor.stripColor(footer);
		if (!footer.startsWith("Server: "))
			return;
		getGameListener().switchLobby(footer.substring("Server: ".length()));
	}

	@Override
	public void onMatch(GameMode gameMode, String key, IPatternResult match) {
		if (key.equals("nick.nicked")) {
			getGameListener().setCurrentNick(match.get(0));
		}
		if (key.equals("nick.unnicked")) {
			getGameListener().setCurrentNick(null);
		}
	}
}
