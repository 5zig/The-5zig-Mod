package eu.the5zig.mod.server;

import eu.the5zig.util.minecraft.ChatColor;

import java.util.Iterator;
import java.util.List;

public class MultiPatternResult implements IMultiPatternResult {

	private final RegisteredServerInstance instance;
	private List<String> messages;

	public MultiPatternResult(RegisteredServerInstance instance, List<String> messages) {
		this.instance = instance;
		this.messages = messages;
	}

	@Override
	public PatternResult parseKey(String key) {
		for (Iterator<String> iterator = messages.iterator(); iterator.hasNext(); ) {
			String message = ChatColor.stripColor(iterator.next());
			final List<String> match = instance.match(message, key);
			if (match == null || match.isEmpty())
				continue;
			iterator.remove();
			return new NonIgnoreablePatternResult(match);
		}
		return null;
	}

	@Override
	public int getRemainingMessageCount() {
		return messages.size();
	}

	@Override
	public String getMessage(int index) {
		return messages.remove(index);
	}
}
