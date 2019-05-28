package eu.the5zig.mod.config;

import com.google.common.collect.Lists;

import java.util.List;

public class TextReplacements {

	private List<TextReplacement> replacements = Lists.newArrayList();

	public TextReplacements() {
		replacements.add(new TextReplacement("\u0026shrug", "\u00af\\_(\u30c4)_/\u00af", true, false));
	}

	public List<TextReplacement> getReplacements() {
		return replacements;
	}
}
