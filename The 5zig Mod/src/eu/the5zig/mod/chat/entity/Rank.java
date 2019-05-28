package eu.the5zig.mod.chat.entity;

import eu.the5zig.util.minecraft.ChatColor;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public enum Rank {

	/**
	 * No color :C
	 */
	NONE('r'),
	/**
	 * Light green color
	 */
	DEFAULT('a'),
	/**
	 * Gold color
	 */
	CUSTOM('6'),
	/**
	 * Red color
	 */
	SPECIAL('5');

	private final char colorCode;

	Rank(char colorCode) {
		this.colorCode = colorCode;
	}

	public String getColorCode() {
		return new String(new char[]{ChatColor.COLOR_CHAR, colorCode});
	}

}