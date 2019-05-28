package eu.the5zig.mod.config;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.Row;

public class TextReplacement implements Row {

	private String message;
	private String replacement;
	private boolean ignoresCommands = true;
	private boolean replaceInsideWords = true;

	public TextReplacement(String message, String replacement, boolean ignoresCommands, boolean replaceInsideWords) {
		this.message = message;
		this.replacement = replacement;
		this.ignoresCommands = ignoresCommands;
		this.replaceInsideWords = replaceInsideWords;
	}

	public TextReplacement() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public boolean isIgnoringCommands() {
		return ignoresCommands;
	}

	public void setIgnoringCommands(boolean ignoresCommands) {
		this.ignoresCommands = ignoresCommands;
	}

	public boolean isReplaceInsideWords() {
		return replaceInsideWords;
	}

	public void setReplaceInsideWords(boolean replaceInsideWords) {
		this.replaceInsideWords = replaceInsideWords;
	}

	@Override
	public void draw(int x, int y) {
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(message, 100), x + 2, y + 2);
		The5zigMod.getVars().drawString("=>", x + 102, y + 2);
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(replacement, 100), x + 115, y + 2);
	}

	@Override
	public int getLineHeight() {
		return 18;
	}
}
