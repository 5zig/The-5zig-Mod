package eu.the5zig.mod.listener;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.JoinText;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.ServerJoinEvent;

import java.util.List;

public class JoinTextListener {

	@EventHandler
	public void onServerJoin(ServerJoinEvent event) {
		List<JoinText> texts = The5zigMod.getJoinTextConfiguration().getConfigInstance().getTexts();
		for (final JoinText text : texts) {
			if (text.getServer() == null || (text.getServerPattern() != null && text.getServerPattern().matcher(event.getHost()).matches())) {
				The5zigMod.getScheduler().postToMainThread(new Runnable() {
					@Override
					public void run() {
						The5zigMod.getListener().onSendChatMessage(text.getMessage());
					}
				}, true, text.getDelay());
			}
		}
	}

}
