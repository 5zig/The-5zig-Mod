package eu.the5zig.mod.modules.items.server.simplehg;

import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.simplehg.ServerSimpleHG;
import eu.the5zig.mod.util.Vector3i;

public class SimpleHGFeast extends GameModeItem<ServerSimpleHG.SimpleHG> {

	public SimpleHGFeast() {
		super(ServerSimpleHG.SimpleHG.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "48, 66, -70 (" + shorten(300.0) + ")";
		}
		ServerSimpleHG.Feast feast = getGameMode().getFeast();
		if (feast == null) {
			return null;
		}

		Vector3i location = feast.getLocation();
		long time = feast.getTime() - System.currentTimeMillis();
		String result = location.getX() + ", " + location.getY() + ", " + location.getZ();
		if (time > 0) {
			result += " (" + shorten((double) time / 1000.0) + ")";
		}
		return result;
	}

	@Override
	public String getTranslation() {
		return "ingame.feast";
	}
}
