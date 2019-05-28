package eu.the5zig.mod.chat.party.handler;

import java.util.Locale;

public class GommeHDHandler extends DefaultHandler {

	@Override
	public boolean match(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("gommehd.net") || host.toLowerCase(Locale.ROOT).endsWith("gommehd.de") || host.toLowerCase(Locale.ROOT).endsWith("gommehd.tk");
	}

}
