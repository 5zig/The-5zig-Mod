package eu.the5zig.mod.chat.party.handler;

import java.util.Locale;

public class CytooxienHandler extends DefaultHandler {

	@Override
	public boolean match(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("cytooxien.de");
	}

	@Override
	public String getPromoteCommand(String name) {
		return null;
	}
}
