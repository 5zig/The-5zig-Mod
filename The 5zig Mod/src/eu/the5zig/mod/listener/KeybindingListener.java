package eu.the5zig.mod.listener;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.items.BoolItem;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.KeyPressEvent;
import eu.the5zig.mod.event.TickEvent;
import eu.the5zig.mod.gui.GuiCoordinatesClipboard;
import eu.the5zig.mod.gui.GuiHypixelStats;
import eu.the5zig.mod.gui.GuiRaidCalculator;
import eu.the5zig.mod.manager.KeybindingManager;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class KeybindingListener {

	private int lastPressed = 0;

	@EventHandler
	public void onTick(TickEvent event) {
		KeybindingManager keybindingManager = The5zigMod.getKeybindingManager();

		if (keybindingManager.toggleMod.isPressed()) {
			The5zigMod.getConfig().get("showMod", BoolItem.class).next();
			The5zigMod.getConfig().save();
		}
		if (keybindingManager.saveCoords.isPressed()) {
			The5zigMod.getVars().displayScreen(new GuiCoordinatesClipboard(null));
		}
		if (keybindingManager.raidTracker.isPressed()) {
			The5zigMod.getVars().displayScreen(new GuiRaidCalculator(null));
		}
		if (lastPressed > 0)
			lastPressed--;
	}

	@EventHandler
	public void onKeyPress(KeyPressEvent event) {
		if (event.getKeyCode() != -1 && event.getKeyCode() == The5zigMod.getKeybindingManager().hypixel.getKeyCode() && lastPressed++ == 0)
			The5zigMod.getVars().displayScreen(new GuiHypixelStats(The5zigMod.getVars().createWrappedGui(The5zigMod.getVars().getMinecraftScreen())));
	}
}
