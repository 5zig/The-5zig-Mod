package eu.the5zig.mod.modules.items.server.playminity;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.playminity.ServerPlayMinity;

public class JumpLeagueCheckpoints extends GameModeItem<ServerPlayMinity.JumpLeague> {

	public JumpLeagueCheckpoints() {
		super(ServerPlayMinity.JumpLeague.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "4/10" : getGameMode().getCheckPoint() + "/" + getGameMode().getMaxCheckPoints();
	}

	@Override
	public String getTranslation() {
		return "ingame.checkpoints";
	}
}
