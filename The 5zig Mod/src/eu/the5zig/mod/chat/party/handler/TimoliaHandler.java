package eu.the5zig.mod.chat.party.handler;

import java.util.Locale;

public class TimoliaHandler extends DefaultHandler {

	@Override
	public boolean match(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("timolia.de");
	}

	@Override
	public String getAcceptCommand(String name) {
		return "/party join " + name;
	}

	@Override
	public String getPromoteCommand(String name) {
		return null;
	}
}
