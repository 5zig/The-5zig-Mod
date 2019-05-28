package eu.the5zig.mod.chat.party.handler;

import java.util.Locale;

public class PlayMinityHandler extends DefaultHandler {

	@Override
	public boolean match(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("playminity.com");
	}

	@Override
	public String getPromoteCommand(String name) {
		return null;
	}
}
