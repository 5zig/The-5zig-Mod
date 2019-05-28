package eu.the5zig.mod.modules.items.server.playminity;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.playminity.ServerPlayMinity;

public class JumpLeagueLives extends GameModeItem<ServerPlayMinity.JumpLeague> {

	public JumpLeagueLives() {
		super(ServerPlayMinity.JumpLeague.class, GameState.ENDGAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? 1 : getGameMode().getLives();
	}

	@Override
	public String getTranslation() {
		return "ingame.lives";
	}
}
