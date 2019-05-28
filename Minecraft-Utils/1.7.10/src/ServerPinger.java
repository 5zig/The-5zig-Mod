import eu.the5zig.mod.util.IServerData;
import eu.the5zig.mod.util.IServerPinger;
import eu.the5zig.util.minecraft.ChatColor;

public class ServerPinger implements IServerPinger {

	private bjq handle = new bjq();

	@Override
	public IServerData createServerData(String serverIP) {
		return new ServerData(serverIP);
	}

	@Override
	public void ping(IServerData iServerData) {
		ServerData serverData = (ServerData) iServerData;
		try {
			handle.a(serverData);
		} catch (Exception e) {
			serverData.e = -1;
			serverData.c = ChatColor.DARK_RED + "Can't connect to server";
		}
	}

	@Override
	public void pingPendingNetworks() {
		handle.a();
	}

	@Override
	public void clearPendingNetworks() {
		handle.b();
	}
}
