package eu.the5zig.mod.config;

import java.io.File;

public class ChatFilterConfiguration extends Configuration<ChatFilter> {

	public ChatFilterConfiguration(File parent) {
		super(new File(parent, "chatFilter.json"), ChatFilter.class);
		for (ChatFilter.ChatFilterMessage chatFilterMessage : configInstance.getChatMessages()) {
			if (chatFilterMessage.getAction() == null)
				chatFilterMessage.setAction(ChatFilter.Action.IGNORE);
			chatFilterMessage.updatePatterns();
		}
		saveConfig();
	}
}
