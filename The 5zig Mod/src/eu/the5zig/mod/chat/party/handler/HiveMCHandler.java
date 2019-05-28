package eu.the5zig.mod.chat.party.handler;

import java.util.Locale;

public class HiveMCHandler implements PartyServerHandler {

	@Override
	public boolean match(String host, int port) {
		return host.toLowerCase(Locale.ROOT).endsWith("hivemc.eu") || host.toLowerCase(Locale.ROOT).endsWith("hivemc.us") || host.toLowerCase(Locale.ROOT).endsWith("hivemc.com");
	}

	@Override
	public String getInviteCommand(String name) {
		return "/party " + name;
	}

	@Override
	public String getAcceptCommand(String name) {
		return "/party accept " + name;
	}

	@Override
	public String getPromoteCommand(String name) {
		return "/party owner " + name;
	}
}
