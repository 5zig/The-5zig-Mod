package eu.the5zig.mod.chat.party.handler;

import java.util.Locale;

public class RewinsideHandler extends DefaultHandler {

	@Override
	public boolean match(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("rewinside.tv");
	}
}
