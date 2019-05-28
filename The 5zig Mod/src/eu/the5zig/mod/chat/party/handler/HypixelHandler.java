package eu.the5zig.mod.chat.party.handler;

public class HypixelHandler extends DefaultHandler {

	@Override
	public boolean match(String host, int port) {
		return host.equalsIgnoreCase("mc.hypixel.net");
	}
}
