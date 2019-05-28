package eu.the5zig.mod.manager.iloveradio;

public class ILoveRadioChannel {

	private final int id;
	private final String name;

	private ILoveRadioTrack currentTrack;

	public ILoveRadioChannel(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ILoveRadioTrack getCurrentTrack() {
		return currentTrack;
	}

	public void setCurrentTrack(ILoveRadioTrack currentTrack) {
		this.currentTrack = currentTrack;
	}
}
