package eu.the5zig.mod.listener;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.chat.entity.Conversation;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TickEvent;
import eu.the5zig.mod.gui.GuiConversations;
import eu.the5zig.mod.util.Display;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class DisplayFocusListener {

	private boolean waitForFocus;

	@EventHandler
	public void onTick(TickEvent event) {
		if (waitForFocus && Display.isActive()) {
			if (The5zigMod.getVars().getCurrentScreen() instanceof GuiConversations) {
				GuiConversations gui = (GuiConversations) The5zigMod.getVars().getCurrentScreen();
				Conversation conversation = gui.getSelectedConversation();
				if (conversation != null) {
					The5zigMod.getConversationManager().setConversationRead(conversation, true);
				}
			}
			waitForFocus = false;
		} else if (!waitForFocus && !Display.isActive()) {
			waitForFocus = true;
		}
	}

}
