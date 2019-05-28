package eu.the5zig.mod.util;

public class PreciseCounter {

	protected int MEASURE_INTERVAL = 1000;

	private double currentCount;
	private Counter[] timers;

	public PreciseCounter() {
		currentCount = 0;
		timers = new Counter[20];
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < timers.length; i++) {
			long plus = startTime + ((long) i * 1000 / timers.length);
			timers[i] = new Counter(MEASURE_INTERVAL, plus);
		}
	}

	public double getCurrentCount() {
		return currentCount;
	}

	public void incrementCount() {
		incrementCount(1);
	}

	public void incrementCount(double add) {
		for (Counter counter : timers) {
			counter.updateCount(add);
		}
	}

	public void update() {
		for (Counter counter : timers) {
			if (counter.isOver()) {
				this.currentCount = (counter.getCount() / ((double) MEASURE_INTERVAL / 1000f));
				counter.updateStartTime();
			}
		}
	}

}
