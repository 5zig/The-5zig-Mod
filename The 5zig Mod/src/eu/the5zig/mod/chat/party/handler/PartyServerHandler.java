package eu.the5zig.mod.chat.party.handler;

public interface PartyServerHandler {

	boolean match(String host, int port);

	String getInviteCommand(String name);

	String getAcceptCommand(String name);

	String getPromoteCommand(String name);

}
