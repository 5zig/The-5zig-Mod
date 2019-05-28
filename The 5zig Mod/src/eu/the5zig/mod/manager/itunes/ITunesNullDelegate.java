package eu.the5zig.mod.manager.itunes;

public class ITunesNullDelegate extends ITunesDelegate {

	@Override
	public boolean isConnected() {
		return false;
	}

	@Override
	public ITunesStatus getStatus() {
		return null;
	}
}
