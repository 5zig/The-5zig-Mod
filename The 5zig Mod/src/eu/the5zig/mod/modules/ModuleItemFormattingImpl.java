package eu.the5zig.mod.modules;

import eu.the5zig.util.minecraft.ChatColor;

public class ModuleItemFormattingImpl implements ModuleItemFormatting {

	private ChatColor prefixFormatting;
	private ChatColor prefixColor;
	private ChatColor mainFormatting;
	private ChatColor mainColor;

	public ModuleItemFormattingImpl(ChatColor prefixFormatting, ChatColor prefixColor, ChatColor mainFormatting, ChatColor mainColor) {
		this.prefixFormatting = prefixFormatting;
		this.prefixColor = prefixColor;
		this.mainFormatting = mainFormatting;
		this.mainColor = mainColor;
	}

	@Override
	public ChatColor getPrefixFormatting() {
		return prefixFormatting;
	}

	public void setPrefixFormatting(ChatColor prefixFormatting) {
		this.prefixFormatting = prefixFormatting;
	}

	@Override
	public ChatColor getPrefixColor() {
		return prefixColor;
	}

	public void setPrefixColor(ChatColor prefixColor) {
		this.prefixColor = prefixColor;
	}

	@Override
	public ChatColor getMainFormatting() {
		return mainFormatting;
	}

	public void setMainFormatting(ChatColor mainFormatting) {
		this.mainFormatting = mainFormatting;
	}

	@Override
	public ChatColor getMainColor() {
		return mainColor;
	}

	public void setMainColor(ChatColor mainColor) {
		this.mainColor = mainColor;
	}
}
