package eu.the5zig.mod.manager;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.WorldTickEvent;

public class TimerManager {

	private long time;
	private boolean running;

	public TimerManager() {
		The5zigMod.getListener().registerListener(this);
	}

	public void toggleStart() {
		time = System.currentTimeMillis() - time;
		running = !running;
	}

	public void reset() {
		running = false;
		time = 0;
	}

	public long getTime() {
		if (!running) {
			return time;
		} else {
			return System.currentTimeMillis() - time;
		}
	}

	@EventHandler
	public void onTick(WorldTickEvent event) {
		if (The5zigMod.getKeybindingManager().timerToggleStart.isPressed()) {
			toggleStart();
		} else if (The5zigMod.getKeybindingManager().timerReset.isPressed()) {
			reset();
		}
	}

}
