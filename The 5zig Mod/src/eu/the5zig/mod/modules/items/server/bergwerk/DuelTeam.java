package eu.the5zig.mod.modules.items.server.bergwerk;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.bergwerk.ServerBergwerk;

public class DuelTeam extends GameModeItem<ServerBergwerk.Duel> {

	public DuelTeam() {
		super(ServerBergwerk.Duel.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		return dummy ? "Red" : getGameMode().getTeam();
	}

	@Override
	public String getTranslation() {
		return "ingame.team";
	}
}
