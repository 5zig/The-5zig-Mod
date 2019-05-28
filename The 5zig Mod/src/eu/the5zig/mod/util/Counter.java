package eu.the5zig.mod.util;

public class Counter {

	private int MEASURE_INTERVAL;

	private long startTime;
	private double count;

	public Counter(int MEASURE_INTERVAL, long startTime) {
		this.MEASURE_INTERVAL = MEASURE_INTERVAL;
		this.startTime = startTime;
		count = 0;
	}

	public void updateCount(double add) {
		count += add;
	}

	public double getCount() {
		return count;
	}

	public boolean isOver() {
		return System.currentTimeMillis() - startTime >= MEASURE_INTERVAL;
	}

	public void updateStartTime() {
		while (isOver()) {
			startTime += MEASURE_INTERVAL;
		}
		count = 0;
	}

}