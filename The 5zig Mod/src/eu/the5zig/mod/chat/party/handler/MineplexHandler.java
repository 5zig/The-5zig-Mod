package eu.the5zig.mod.chat.party.handler;

import java.util.Locale;

public class MineplexHandler implements PartyServerHandler {

	@Override
	public boolean match(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("mineplex.com") || host.toLowerCase(Locale.ROOT).endsWith("mineplex.eu") || host.toLowerCase(Locale.ROOT).endsWith("mineplex.us");
	}

	@Override
	public String getInviteCommand(String name) {
		return "/party " + name;
	}

	@Override
	public String getAcceptCommand(String name) {
		return "/partyaccept " + name;
	}

	@Override
	public String getPromoteCommand(String name) {
		return null;
	}
}
