package eu.the5zig.mod.modules.items.server.hypixel;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.modules.LargeTextItem;
import eu.the5zig.mod.server.hypixel.ServerHypixel;

public class BlitzDeathmatchMessage extends LargeTextItem<ServerHypixel.Blitz> {

	public BlitzDeathmatchMessage() {
		super(ServerHypixel.Blitz.class);
	}

	@Override
	protected String getText() {
		if (getGameMode().getWinner() == null && getGameMode().getDeathmatch() != -1 && getGameMode().getDeathmatch() - System.currentTimeMillis() > 0) {
			String time = shorten((getGameMode().getDeathmatch() - System.currentTimeMillis()) / 1000.0);
			if (getGameMode().getDeathmatch() - System.currentTimeMillis() < 1000 * 15) {
				return I18n.translate("ingame.deathmatch_in", time);
			}
		}
		return null;
	}
}
