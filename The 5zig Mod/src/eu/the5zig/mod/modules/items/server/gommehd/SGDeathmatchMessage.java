package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.modules.LargeTextItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class SGDeathmatchMessage extends LargeTextItem<ServerGommeHD.SurvivalGames> {

	public SGDeathmatchMessage() {
		super(ServerGommeHD.SurvivalGames.class);
	}

	@Override
	protected String getText() {
		if (getGameMode().getDeathmatchTime() != -1 && getGameMode().getDeathmatchTime() - System.currentTimeMillis() > 0 &&
				getGameMode().getDeathmatchTime() - System.currentTimeMillis() <= 1000 * 15) {
			return I18n.translate("ingame.deathmatch_in", shorten((double) (getGameMode().getDeathmatchTime() - System.currentTimeMillis()) / 1000.0));
		}
		return null;
	}

}
