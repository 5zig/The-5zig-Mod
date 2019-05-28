package eu.the5zig.mod.server;

import com.google.common.collect.Sets;
import com.google.common.net.InternetDomainName;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Friend;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public class GameServer extends Server implements IGameServer {

	private String configName;
	/**
	 * Game lobby. Eg. SG2414
	 */
	protected String lobby;
	protected transient GameMode gameMode;
	private transient String nickname;

	private final transient Set<String> onlineFriends = Sets.newHashSet();
	private final transient Set<String> partyMembers = Sets.newHashSet();

	public GameServer() {
		super();
		loadFriends();
	}

	public GameServer(String host, int port, String configName) {
		super(host, port);
		this.configName = configName;
		loadFriends();
	}

	protected void loadFriends() {
		String host = getHost();
		if (host == null) {
			return;
		}
		for (Friend friend : The5zigMod.getFriendManager().getFriends()) {
			checkFriendServer(friend);
		}
	}

	public void checkFriendServer(Friend friend) {
		getOnlineFriends().remove(friend.getUsername());
		String friendServer = friend.getServer();
		if (friendServer == null || friendServer.equals("Hidden")) {
			return;
		}
		String friendHost = friendServer.split(":")[0];
		if (StringUtils.equals(friendHost, getHost())) {
			getOnlineFriends().add(friend.getUsername());
		} else {
			boolean selfValid = InternetDomainName.isValid(getHost());
			boolean friendValid = InternetDomainName.isValid(friendHost);
			if (selfValid && friendValid) {
				InternetDomainName mainDomain = InternetDomainName.from(getHost());
				InternetDomainName friendDomain = InternetDomainName.from(friendHost);
				boolean mainPublic = mainDomain.isUnderPublicSuffix();
				boolean friendPublic = friendDomain.isUnderPublicSuffix();
				if (mainPublic && friendPublic) {
					String mainServerDomain = mainDomain.topPrivateDomain().toString();
					String friendMainServerDomain = friendDomain.topPrivateDomain().toString();
					if (mainServerDomain.equals(friendMainServerDomain)) {
						getOnlineFriends().add(friend.getUsername());
					}
				}
			}
		}
	}

	public String getConfigName() {
		return configName;
	}

	@Override
	public String getLobby() {
		return lobby;
	}

	@Override
	public void setLobby(String lobby) {
		this.lobby = lobby;
	}

	@Override
	public GameMode getGameMode() {
		return gameMode;
	}

	@Override
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	@Override
	public Set<String> getOnlineFriends() {
		return onlineFriends;
	}

	@Override
	public Set<String> getPartyMembers() {
		return partyMembers;
	}

	@Override
	public String getLobbyString() {
		return getGameMode() == null ? getLobby() == null ? "" : getLobby() : getGameMode().getName() + "/" + getLobby();
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
