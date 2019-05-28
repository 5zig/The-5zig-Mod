package eu.the5zig.mod.chat.party.handler;

import java.util.Locale;

public class DustMCHandler implements PartyServerHandler {

	@Override
	public boolean match(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("dustmc.de");
	}

	@Override
	public String getInviteCommand(String name) {
		return "/party " + name;
	}

	@Override
	public String getAcceptCommand(String name) {
		return "/party " + name;
	}

	@Override
	public String getPromoteCommand(String name) {
		return null;
	}
}
