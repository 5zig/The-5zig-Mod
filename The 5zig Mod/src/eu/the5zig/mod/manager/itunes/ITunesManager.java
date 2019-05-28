package eu.the5zig.mod.manager.itunes;

import eu.the5zig.util.Utils;

public class ITunesManager {

	public static final int ITUNES_COLOR_ACCENT = 0xFF606060;
	public static final int ITUNES_COLOR_SECOND = 0xFF979797;
	public static final int ITUNES_COLOR_BACKGROUND = 0xFFC6C6C6;

	private final ITunesDelegate delegate;

	public ITunesManager() {
		switch (Utils.getPlatform()) {
			case MAC:
				delegate = new ITunesMacDelegate();
				break;
			case WINDOWS:
				delegate = new ITunesWindowsDelegate();
				break;
			default:
				delegate = new ITunesNullDelegate();
				break;
		}
	}

	public ITunesDelegate getDelegate() {
		return delegate;
	}
}
