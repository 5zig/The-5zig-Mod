package eu.the5zig.mod.config;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.Row;
import eu.the5zig.util.Utils;

import java.util.regex.Pattern;

public class JoinText implements Row {

	private transient Pattern serverPattern;

	private String server;
	private String message;
	private int delay;

	public JoinText() {
	}

	public JoinText(String server, String message, int delay) {
		setServer(server);
		this.message = message;
		this.delay = delay;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
		if (server == null) {
			serverPattern = null;
		} else {
			try {
				serverPattern = Utils.compileMatchPattern(server);
			} catch (Exception e) {
				The5zigMod.logger.error("Could not compile pattern: " + server + "!", e);
			}
		}
	}

	public Pattern getServerPattern() {
		return serverPattern;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public void draw(int x, int y) {
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(message, 100), x + 2, y + 2);
		The5zigMod.getVars().drawString(":", x + 102, y + 2);

		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(server, 100), x + 115, y + 2);
	}

	@Override
	public int getLineHeight() {
		return 18;
	}
}
