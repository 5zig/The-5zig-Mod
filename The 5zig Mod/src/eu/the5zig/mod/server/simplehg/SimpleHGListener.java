package eu.the5zig.mod.server.simplehg;

import eu.the5zig.mod.server.AbstractGameListener;
import eu.the5zig.mod.server.GameMode;
import eu.the5zig.mod.server.IPatternResult;

public class SimpleHGListener extends AbstractGameListener<GameMode> {

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
			getGameListener().switchLobby(match.get(0));
		}
		if (key.equals("nick.nicked")) {
			getGameListener().setCurrentNick(match.get(0));
		}
		if (key.equals("nick.unnicked")) {
			getGameListener().setCurrentNick(null);
		}
	}
}
