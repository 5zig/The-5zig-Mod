package eu.the5zig.mod.modules.items.server.timolia;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.modules.GameModeItem;
import eu.the5zig.mod.server.GameState;
import eu.the5zig.mod.server.timolia.ServerTimolia;
import eu.the5zig.mod.util.Vector3f;

public class JumpWorldLastCheckpoint extends GameModeItem<ServerTimolia.JumpWorld> {

	public JumpWorldLastCheckpoint() {
		super(ServerTimolia.JumpWorld.class, GameState.GAME);
	}

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return shorten(10.0) + " m";
		}
		Vector3f lastCheckpoint = getGameMode().getLastCheckpoint();
		if (lastCheckpoint == null) {
			return null;
		}
		float distance = lastCheckpoint.distanceSquared((float) The5zigMod.getVars().getPlayerPosX(), (float) The5zigMod.getVars().getPlayerPosY(), (float) The5zigMod.getVars().getPlayerPosZ());
		return shorten(Math.sqrt(distance)) + " m";
	}

	@Override
	public String getTranslation() {
		return "ingame.last_checkpoint";
	}
}
