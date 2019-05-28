import eu.the5zig.mod.util.IServerData;

public class ServerData extends cqy implements IServerData {

	public ServerData(String serverIP) {
		super(serverIP, serverIP, false);
	}

	@Override
	public String getServerName() {
		return a;
	}

	@Override
	public String getServerIP() {
		return b;
	}

	@Override
	public String getPopulationInfo() {
		return c;
	}

	@Override
	public String getMOTD() {
		return d;
	}

	@Override
	public long getPing() {
		return e;
	}

	@Override
	public String getServerIcon() {
		return c();
	}
}
