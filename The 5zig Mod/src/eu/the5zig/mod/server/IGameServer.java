package eu.the5zig.mod.server;

import java.util.Set;

public interface IGameServer extends IServer {

	String getLobby();

	void setLobby(String lobby);

	GameMode getGameMode();

	void setGameMode(GameMode gameMode);

	String getLobbyString();

	Set<String> getOnlineFriends();

	Set<String> getPartyMembers();

	String getNickname();

	void setNickname(String nickname);
}
