package eu.the5zig.mod.render;

import eu.the5zig.mod.The5zigMod;

public class FormattingImpl implements Formatting {

	@Override
	public String getPrefixFormatting() {
		return The5zigMod.getRenderer().getPrefix();
	}

	@Override
	public String getMainFormatting() {
		return The5zigMod.getRenderer().getMain();
	}

	@Override
	public String getBracketFormatting() {
		return The5zigMod.getRenderer().getBrackets();
	}
}
