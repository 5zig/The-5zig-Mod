package eu.the5zig.mod.modules.items.server;

public class Nickname extends ServerItem {

	@Override
	protected Object getValue(boolean dummy) {
		if (dummy) {
			return "5zig";
		}
		return getServer().getNickname();
	}

	@Override
	public String getTranslation() {
		return "ingame.nickname";
	}
}
