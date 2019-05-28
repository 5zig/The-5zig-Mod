package eu.the5zig.mod.modules;

import eu.the5zig.util.minecraft.ChatColor;

public class ModuleLabelFormatting {

	private ChatColor mainFormatting;
	private ChatColor mainColor;

	public ModuleLabelFormatting(ChatColor mainFormatting, ChatColor mainColor) {
		this.mainFormatting = mainFormatting;
		this.mainColor = mainColor;
	}

	public ChatColor getMainFormatting() {
		return mainFormatting;
	}

	public void setMainFormatting(ChatColor mainFormatting) {
		this.mainFormatting = mainFormatting;
	}

	public ChatColor getMainColor() {
		return mainColor;
	}

	public void setMainColor(ChatColor mainColor) {
		this.mainColor = mainColor;
	}

}
