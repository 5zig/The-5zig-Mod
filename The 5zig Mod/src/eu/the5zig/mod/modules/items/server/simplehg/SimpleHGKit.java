package eu.the5zig.mod.modules.items.server.simplehg;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.simplehg.ServerSimpleHG;

public class SimpleHGKit extends GameModeItem<ServerSimpleHG.SimpleHG> {

	public SimpleHGKit() {
		super(ServerSimpleHG.SimpleHG.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Viking" : getGameMode().getKit();
	}

	@Override
	public String getTranslation() {
		return "ingame.kit";
	}
}
