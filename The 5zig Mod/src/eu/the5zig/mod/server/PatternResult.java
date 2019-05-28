package eu.the5zig.mod.server;

import java.util.List;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 * <p/>
 * Class for Pattern Matching Result, used in ServerListener to avoid IndexArrayOutOfBoundsException.
 */
public class PatternResult implements IPatternResult {

	private final List<String> result;
	private boolean ignoreMessage = false;

	public PatternResult(List<String> result) {
		this.result = result;
	}

	public int size() {
		return result.size();
	}

	public String get(int index) {
		if (index < 0 || index >= size())
			return "";
		return result.get(index);
	}

	@Override
	public void ignoreMessage(boolean ignore) {
		this.ignoreMessage = ignore;
	}

	boolean shouldMessageBeIgnored() {
		return ignoreMessage;
	}

	@Override
	public String toString() {
		return result.toString();
	}
}
