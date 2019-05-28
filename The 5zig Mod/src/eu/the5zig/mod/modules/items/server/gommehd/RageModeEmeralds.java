package eu.the5zig.mod.modules.items.server.gommehd;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.gomme.ServerGommeHD;

public class RageModeEmeralds extends GameModeItem<ServerGommeHD.RageMode> {

	public RageModeEmeralds() {
		super(ServerGommeHD.RageMode.class);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 23;
		}
		return getGameMode().getEmeralds() > 0 ? getGameMode().getEmeralds() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.emeralds";
	}
}
