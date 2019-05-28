package eu.the5zig.mod.chat.party.handler;

public abstract class DefaultHandler implements PartyServerHandler {

	@Override
	public String getInviteCommand(String name) {
		return "/party invite " + name;
	}

	@Override
	public String getAcceptCommand(String name) {
		return "/party accept " + name;
	}

	@Override
	public String getPromoteCommand(String name) {
		return "/party promote " + name;
	}
}
