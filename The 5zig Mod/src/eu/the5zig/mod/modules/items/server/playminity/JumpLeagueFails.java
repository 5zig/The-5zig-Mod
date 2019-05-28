package eu.the5zig.mod.modules.items.server.playminity;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.playminity.ServerPlayMinity;

public class JumpLeagueFails extends GameModeItem<ServerPlayMinity.JumpLeague> {

	public JumpLeagueFails() {
		super(ServerPlayMinity.JumpLeague.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? 1 : getGameMode().getFails();
	}

	@Override
	public String getTranslation() {
		return "ingame.fails";
	}
}
