package eu.the5zig.mod.manager;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.util.Counter;

public class SpeedCalculator {

	private double currentSpeed;
	private SpeedCounter[] timers;

	public SpeedCalculator() {
		currentSpeed = 0;
		timers = new SpeedCounter[20];
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < timers.length; i++) {
			long plus = startTime + ((long) i * 1000 / timers.length);
			timers[i] = new SpeedCounter(1000, plus);
		}
	}

	public double getCurrentSpeed() {
		return currentSpeed;
	}

	public void update() {
		for (SpeedCounter counter : timers) {
			if (!The5zigMod.getVars().isPlayerNull() && !The5zigMod.getVars().isTerrainLoading()) {
				if (counter.isOver()) {
					double x = The5zigMod.getVars().getPlayerPosX();
					double y = The5zigMod.getVars().getPlayerPosY();
					double z = The5zigMod.getVars().getPlayerPosZ();

					if (counter.lastX != null && counter.lastY != null && counter.lastZ != null) {
						currentSpeed = Math.sqrt((counter.lastX - x) * (counter.lastX - x) + (counter.lastY - y) * (counter.lastY - y) + (counter.lastZ - z) * (counter.lastZ - z));
						if (currentSpeed > 100) {
							currentSpeed = 0;
						}
					} else {
						currentSpeed = 0;
					}
					counter.lastX = x;
					counter.lastY = y;
					counter.lastZ = z;

					counter.updateStartTime();
				}
			} else {
				counter.lastX = null;
				counter.lastY = null;
				counter.lastZ = null;
			}
		}
	}

	public class SpeedCounter extends Counter {

		private Double lastX, lastY, lastZ;

		public SpeedCounter(int MEASURE_INTERVAL, long startTime) {
			super(MEASURE_INTERVAL, startTime);
		}
	}
}
