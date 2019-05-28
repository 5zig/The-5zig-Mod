package eu.the5zig.mod.modules.items.server.hypixel;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.hypixel.ServerHypixel;

public class PaintballTeam extends GameModeItem<ServerHypixel.Paintball> {

	public PaintballTeam() {
		super(ServerHypixel.Paintball.class);
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
