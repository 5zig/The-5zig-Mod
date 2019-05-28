package eu.the5zig.mod.config;

import java.io.File;

public class TextReplacementConfiguration extends Configuration<TextReplacements> {

	public TextReplacementConfiguration(File parent) {
		super(new File(parent, "textReplacements.json"), TextReplacements.class);
	}

}
