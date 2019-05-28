package eu.the5zig.mod.server.mcpvp;

import java.util.concurrent.TimeUnit;

public class Feast {

	private int x, z;
	private long millisStarted;
	private int feastTime;

	public Feast(int x, int z) {
		this.x = x;
		this.z = z;
		millisStarted = System.currentTimeMillis();
		feastTime = 5 * 1000 * 60;
		feastTime += 2000;
	}

	public Feast(int x, int z, int feastTime) {
		this.x = x;
		this.z = z;
		millisStarted = System.currentTimeMillis();
		this.feastTime = feastTime;
		this.feastTime += 2000;
	}

	public String getRemainingTime() {
		long millis = (millisStarted + feastTime) - System.currentTimeMillis();
		if (millis > 999) {
			return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
					TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		}
		return null;
	}

	public String getCoordinates() {
		return x + ", " + z;
	}

}