package eu.the5zig.mod.modules.items.server;

public class Deaths extends ServerItem {
	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return 1;
		}
		return getServer().getGameMode() != null && getServer().getGameMode().isRespawnable() && getServer().getGameMode().getDeaths() > 0 ? getServer().getGameMode().getDeaths() : null;
	}

	@Override
	public String getTranslation() {
		return "ingame.deaths";
	}
}
