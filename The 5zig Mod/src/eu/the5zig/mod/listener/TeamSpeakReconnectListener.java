package eu.the5zig.mod.listener;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TickEvent;
import eu.the5zig.teamspeak.TeamSpeak;

public class TeamSpeakReconnectListener {

	private boolean reconnecting;
	private int reconnectTicks = 1;
	private boolean enabled;

	@EventHandler
	public void onTick(TickEvent event) {
		if (!The5zigMod.getConfig().getBool("tsEnabled")) {
			enabled = false;
			if (TeamSpeak.getClient().isConnected()) {
				TeamSpeak.getClient().disconnect();
			}
			return;
		}
		if (!enabled) {
			reconnectTicks = 1;
		}
		enabled = true;
		if (!TeamSpeak.getClient().isConnected() && !reconnecting && reconnectTicks == 0) {
			reconnectTicks = 20 * 10;
		} else if (!The5zigMod.getDataManager().isTsRequiresAuth() && !TeamSpeak.getClient().isConnected() && reconnectTicks != 0
				&& --reconnectTicks == 0) {
			reconnecting = true;
			The5zigMod.getAsyncExecutor().execute(new Runnable() {
				@Override
				public void run() {
					try {
						TeamSpeak.getClient().connect(The5zigMod.getConfig().getString("tsAuthKey"));
					} catch (Throwable throwable) {
						The5zigMod.logger.error("Could not connect to TeamSpeak Client!", throwable);
					}
					reconnecting = false;
				}
			});
		}
	}

	public void reconnectNow() {
		reconnectTicks = 1;
	}

}
