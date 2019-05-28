package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class SGDeathmatch extends GameModeItem<ServerGommeHD.SurvivalGames> {

	public SGDeathmatch() {
		super(ServerGommeHD.SurvivalGames.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return shorten(10.0);
		}
		return getGameMode().getDeathmatchTime() != -1 && getGameMode().getDeathmatchTime() - System.currentTimeMillis() > 0 ? shorten(
				(double) (getGameMode().getDeathmatchTime() - System.currentTimeMillis()) / 1000.0) : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.deathmatch";
	}
}
