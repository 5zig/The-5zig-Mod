package eu.the5zig.mod.modules.items.server.bergwerk;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.bergwerk.ServerBergwerk;

public class DuelRespawn extends GameModeItem<ServerBergwerk.Duel> {

	public DuelRespawn() {
		super(ServerBergwerk.Duel.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy || getGameMode().isCanRespawn() ? I18n.translate("ingame.can_respawn.true") : I18n.translate("ingame.can_respawn.false");
	}

	@Override
	public String getTranslation() {
		return "ingame.can_respawn";
	}
}
