package eu.the5zig.mod.listener;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.WorldTickEvent;
import eu.the5zig.mod.util.Keyboard;

public class ChatSearchManager {

	private boolean searching;
	private boolean keyPressed;

	public ChatSearchManager() {
		The5zigMod.getListener().registerListener(this);
	}

	@EventHandler
	public void onTick(WorldTickEvent event) {
		if (The5zigMod.getVars().isChatOpened()) {
			boolean down = The5zigMod.isCtrlKeyDown() && Keyboard.isKeyDown(Keyboard.KEY_F);
			if (!keyPressed && down) {
				searching = !searching;
				keyPressed = true;
			} else if (!down) {
				keyPressed = false;
			}
		} else {
			searching = false;
		}
	}

	public boolean isSearching() {
		return searching;
	}

	public String getSearchText() {
		return The5zigMod.getVars().getChatBoxText();
	}
}
