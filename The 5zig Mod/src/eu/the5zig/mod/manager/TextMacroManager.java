package eu.the5zig.mod.manager;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.TextMacro;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.WorldTickEvent;
import eu.the5zig.mod.util.Keyboard;

import java.util.List;

public class TextMacroManager {

	@EventHandler
	public void onTick(WorldTickEvent event) {
		if (The5zigMod.getVars().isPlayerNull() || The5zigMod.getVars().getMinecraftScreen() != null) {
			return;
		}
		List<TextMacro> macros = The5zigMod.getTextMacroConfiguration().getConfigInstance().getMacros();
		for (TextMacro macro : macros) {
			int pressed = 0;
			for (Integer key : macro.getKeys()) {
				if (Keyboard.isKeyDown(key)) {
					pressed++;
				}
			}
			if (pressed == macro.getKeys().size()) {
				if (!macro.pressed) {
					String autoText = macro.getMessage();
					autoText = ChatFilterManager.replacePlaceholders(autoText);
					if (macro.isAutoSend()) {
						The5zigMod.getListener().onSendChatMessage(autoText);
					} else {
						The5zigMod.getVars().typeInChatGUI(autoText);
					}
					macro.pressed = true;
				}
			} else {
				macro.pressed = false;
			}
		}
	}

}
