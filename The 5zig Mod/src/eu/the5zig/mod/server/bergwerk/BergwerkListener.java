package eu.the5zig.mod.server.bergwerk;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.util.minecraft.ChatColor;

public class BergwerkListener extends AbstractGameListener<GameMode> {

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
		if (!footer.startsWith("Du befindest dich auf: "))
			return;
		String lobby = footer.split(": |\n")[1];
		getGameListener().switchLobby(lobby);
	}

}
