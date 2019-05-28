package eu.the5zig.mod.manager;

import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.config.TextReplacement;
import eu.the5zig.mod.event.ChatSendEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.SignEditEvent;

import java.util.List;

public class TextReplacementManager {

	@EventHandler
	public void onSendChatMessage(ChatSendEvent event) {
		event.setMessage(replaceText(event.getMessage()));
	}

	@EventHandler
	public void onSignEdit(SignEditEvent event) {
		for (int i = 0; i < event.getLines().length; i++) {
			event.setLine(i, replaceText(event.getLine(i)));
		}
	}

	public static String replaceText(String text) {
		List<TextReplacement> replacements = The5zigMod.getTextReplacementConfig().getConfigInstance().getReplacements();
		for (TextReplacement replacement : replacements) {
			if (text.startsWith("/") && replacement.isIgnoringCommands()) {
				continue;
			}
			int index;
			int nextIndex = 0;
			while ((index = text.indexOf(replacement.getMessage(), nextIndex)) != -1) {
				nextIndex = index + replacement.getMessage().length();
				if (!replacement.isReplaceInsideWords()) {
					if (index > 0) {
						char previousChar = Character.toLowerCase(text.charAt(index - 1));
						if (previousChar != ' ') {
							continue;
						}
					}
					if (index + replacement.getMessage().length() < text.length()) {
						char nextChar = text.charAt(index + replacement.getMessage().length());
						if (nextChar != ' ') {
							continue;
						}
					}

				}
				nextIndex = index + replacement.getReplacement().length();
				text = text.substring(0, index) + replacement.getReplacement() + text.substring(index + replacement.getMessage().length(),
						text.length());
			}
		}
		return text;
	}

}
