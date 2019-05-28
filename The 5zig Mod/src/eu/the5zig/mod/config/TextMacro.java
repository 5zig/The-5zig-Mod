package eu.the5zig.mod.config;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.Row;

import java.util.List;

public class TextMacro implements Row {

	private List<Integer> keys;
	private String message;
	private boolean autoSend = true;

	public transient boolean pressed;

	public TextMacro() {
	}

	public TextMacro(List<Integer> keys, String message, boolean autoSend) {
		this.keys = keys;
		this.message = message;
		this.autoSend = autoSend;
	}

	public List<Integer> getKeys() {
		return keys;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAutoSend() {
		return autoSend;
	}

	public void setAutoSend(boolean autoSend) {
		this.autoSend = autoSend;
	}

	public String getKeysAsString() {
		StringBuilder macroText = new StringBuilder();
		for (int key : keys) {
			if (macroText.length() != 0) {
				macroText.append(" + ");
			}
			macroText.append(The5zigMod.getVars().getKeyDisplayStringShort(key));
		}
		return macroText.toString();
	}

	@Override
	public void draw(int x, int y) {
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(message, 100), x + 2, y + 2);
		The5zigMod.getVars().drawString(":", x + 102, y + 2);

		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(getKeysAsString(), 100), x + 115, y + 2);
	}

	@Override
	public int getLineHeight() {
		return 18;
	}
}
