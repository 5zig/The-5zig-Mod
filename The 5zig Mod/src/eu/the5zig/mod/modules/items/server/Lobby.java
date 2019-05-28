package eu.the5zig.mod.modules.items.server;

public class Lobby extends ServerItem {
	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "Lobby01";
		}
		return getServer().getLobby();
	}

	@Override
	public String getTranslation() {
		return "ingame.lobby";
	}
}
