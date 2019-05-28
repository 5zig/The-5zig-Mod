package eu.the5zig.mod.server.gomme;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.server.*;
import eu.the5zig.util.minecraft.ChatColor;

public class GommeHDListener extends AbstractGameListener<GameMode> {

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
		if (key.equals("nick.nicked.1") || key.equals("nick.nicked.2")) {
			getGameListener().setCurrentNick(match.get(0));
		}
		if (key.equals("nick.unnicked")) {
			getGameListener().setCurrentNick(null);
		}
		if (gameMode == null)
			return;
		if (gameMode instanceof Teamable) {
			if (key.equals("teams.allowed")) {
				((Teamable) gameMode).setTeamsAllowed(true);
			}
			if (key.equals("teams.not_allowed")) {
				((Teamable) gameMode).setTeamsAllowed(false);
			}
		}
		if (gameMode.getState() == GameState.GAME) {
			if (key.equals("kill") && match.get(1).equals(The5zigMod.getDataManager().getUsername())) {
				gameMode.setKills(gameMode.getKills() + 1);
				gameMode.setKillStreak(gameMode.getKillStreak() + 1);
			}
			if (key.equals("death") || (key.equals("kill") && match.get(0).equals(The5zigMod.getDataManager().getUsername()))) {
				gameMode.setDeaths(gameMode.getDeaths() + 1);
				gameMode.setKillStreak(0);
			}
		}
	}

	@Override
	public void onPlayerListHeaderFooter(GameMode gameMode, String header, String footer) {
		header = ChatColor.stripColor(header);
		if (header.startsWith("GommeHD.net ")) {
			getGameListener().switchLobby(header.split("GommeHD.net |\n")[1]);
		}
	}

}
