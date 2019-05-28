package eu.the5zig.mod.modules.items.server;

public class Kills extends ServerItem {
	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 1;
		}
		return getServer().getGameMode() != null && getServer().getGameMode().getKills() > 0 ? getServer().getGameMode().getKills() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.kills";
	}
}
