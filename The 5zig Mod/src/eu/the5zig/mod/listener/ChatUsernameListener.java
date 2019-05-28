package eu.the5zig.mod.listener;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.util.Display;
import eu.the5zig.util.minecraft.ChatColor;

public class ChatUsernameListener {

	@EventHandler
	public boolean onServerChat(ChatEvent event) {
		String message = event.getMessage();
		if (message.contains(The5zigMod.getDataManager().getUsername()) && !Display.isActive() && The5zigMod.getConfig().getBool("notifyOnName")) {
			The5zigMod.getTrayManager().displayMessage("The 5zig Mod - " + I18n.translate("ingame_chat.new_message"), ChatColor.stripColor(message));
		}
		return false;
	}
}
