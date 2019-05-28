package eu.the5zig.mod.manager.itunes;

public class ITunesStatus {

	private boolean running;
	private boolean playing;
	private ITunesTrack track;
	private double playingPosition;

	private transient long serverTime;

	public ITunesStatus() {
	}

	public ITunesStatus(boolean playing, ITunesTrack track, double playingPosition) {
		this.running = true;
		this.playing = playing;
		this.track = track;
		this.playingPosition = playingPosition;
		this.serverTime = System.currentTimeMillis();
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isPlaying() {
		return playing;
	}

	public ITunesTrack getTrack() {
		return track;
	}

	public double getPlayingPosition() {
		return playingPosition;
	}

	public void setPlayingPosition(double playingPosition) {
		this.playingPosition = playingPosition;
	}

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}

	@Override
	public String toString() {
		return "ITunesStatus{" +
				"running=" + running +
				", playing=" + playing +
				", track=" + track +
				", playingPosition=" + playingPosition +
				'}';
	}
}
