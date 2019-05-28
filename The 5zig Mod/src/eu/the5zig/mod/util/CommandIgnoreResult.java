package eu.the5zig.mod.util;

import com.google.common.collect.Lists;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.util.minecraft.ChatColor;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class CommandIgnoreResult {

	private List<Result> messagesToIgnore = Lists.newArrayList();

	public boolean handle(String message) {
		if (messagesToIgnore.isEmpty())
			return false;
		message = ChatColor.stripColor(message);

		if (messagesToIgnore.get(0).getResult().matcher(message).matches()) {
			The5zigMod.logger.debug("Ignored Chat Message {}!", message);
			messagesToIgnore.remove(0);
			return true;
		}
		return false;
	}

	public void send(String command, String ignorePattern) {
		send(command, Pattern.compile(ignorePattern));
	}

	public void send(String command, Pattern ignorePattern) {
		if (The5zigMod.getVars().isPlayerNull()) {
			The5zigMod.logger.warn("Could not send command " + command);
			return;
		}
		The5zigMod.getVars().sendMessage(command);
		messagesToIgnore.add(new Result(ignorePattern));
	}

	public class Result {

		private final Pattern result;
		private final long time;

		public Result(Pattern result) {
			this.result = result;
			this.time = System.currentTimeMillis() + 1000 * 10;
		}

		public Pattern getResult() {
			return result;
		}

		public long getTime() {
			return time;
		}

		@Override
		public String toString() {
			return result.pattern();
		}
	}

}
