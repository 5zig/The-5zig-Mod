package eu.the5zig.mod.server;

import java.util.List;

public class NonIgnoreablePatternResult extends PatternResult {

	public NonIgnoreablePatternResult(List<String> result) {
		super(result);
	}

	@Override
	public void ignoreMessage(boolean ignore) {
		throw new UnsupportedOperationException("Cannot ignore this result anymore!");
	}
}
