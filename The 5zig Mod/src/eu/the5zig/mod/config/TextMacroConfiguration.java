package eu.the5zig.mod.config;

import java.io.File;

public class TextMacroConfiguration extends Configuration<TextMacros> {

	public TextMacroConfiguration(File parent) {
		super(new File(parent, "textMacros.json"), TextMacros.class);
	}

}
