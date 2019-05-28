package eu.the5zig.mod.modules.items.server.bergwerk;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.modules.LargeTextItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.bergwerk.ServerBergwerk;

public class DuelTeleportMessage extends LargeTextItem<ServerBergwerk.Duel> {

	public DuelTeleportMessage() {
		super(ServerBergwerk.Duel.class, GameState.GAME);
	}

	@Override
	protected String getText() {
		if (getGameMode().getTeleporterTimer() != -1 && getGameMode().getTeleporterTimer() - System.currentTimeMillis() > 0) {
			return I18n.translate("ingame.duel_teleporter");
		}
		return null;
	}

}
