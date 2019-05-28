package eu.the5zig.mod.listener;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TickEvent;
import eu.the5zig.mod.util.IKeybinding;
import eu.the5zig.mod.util.Keyboard;
import eu.the5zig.mod.util.Mouse;

public class ZoomListener {

	private boolean zoomed = false;
	private float previousFOV;
	private boolean previousSmoothCamera;

	@EventHandler
	public void onTick(TickEvent event) {
		if (The5zigMod.getVars().getMinecraftScreen() == null && isKeyDown(The5zigMod.getKeybindingManager().zoom)) {
			if (!zoomed) {
				zoomed = true;
				previousFOV = The5zigMod.getVars().getFOV();
				previousSmoothCamera = The5zigMod.getVars().isSmoothCamera();

				The5zigMod.getVars().setFOV(previousFOV / The5zigMod.getConfig().getFloat("zoomFactor"));
				The5zigMod.getVars().setSmoothCamera(true);
			}
		} else {
			if (zoomed) {
				zoomed = false;

				The5zigMod.getVars().setFOV(previousFOV);
				The5zigMod.getVars().setSmoothCamera(previousSmoothCamera);
			}
		}
	}

	private boolean isKeyDown(IKeybinding keybinding) {
		return keybinding.getKeyCode() != 0 && (keybinding.getKeyCode() < 0 ? Mouse.isButtonDown(keybinding.getKeyCode() + 100) : keybinding.getKeyCode() < 256 && Keyboard.isKeyDown(
				keybinding.getKeyCode()));
	}

}
