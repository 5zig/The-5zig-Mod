package eu.the5zig.mod.manager.itunes;

public abstract class ITunesDelegate {

	protected ITunesDelegate() {
	}

	public abstract boolean isConnected();

	public abstract ITunesStatus getStatus();
}
