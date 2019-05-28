package eu.the5zig.mod.modules.items.server.hypixel;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.modules.LargeTextItem;
import eu.the5zig.mod.server.hypixel.ServerHypixel;

public class BlitzStarMessage extends LargeTextItem<ServerHypixel.Blitz> {

	public BlitzStarMessage() {
		super(ServerHypixel.Blitz.class);
	}

	@Override
	protected String getText() {
		if (getGameMode().getWinner() == null && getGameMode().getStar() != -1 && getGameMode().getStar() - System.currentTimeMillis() > 0) {
			String time = shorten((getGameMode().getStar() - System.currentTimeMillis()) / 1000.0);
			if (getGameMode().getStar() - System.currentTimeMillis() < 1000 * 15) {
				return I18n.translate("ingame.star_in", time);
			}
		}
		return null;
	}
}
