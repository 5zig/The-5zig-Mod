package eu.the5zig.mod.gui;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.elements.Row;
import eu.the5zig.util.Container;
import eu.the5zig.util.minecraft.ChatColor;

/**
 * Created by 5zig.
 * All rights reserved © 2015
 */
public class StatisticRow implements Row {

	private String translation;
	private Container[] containers;
	private int xOff;
	private int yOff;

	public StatisticRow(String translation, Container... containers) {
		this(translation, 0, 0, containers);
	}

	public StatisticRow(String translation, int xOff, int yOff, Container... containers) {
		this.translation = translation;
		this.containers = containers;
		this.xOff = xOff;
		this.yOff = yOff;
	}

	@Override
	public int getLineHeight() {
		return 14;
	}

	@Override
	public void draw(int x, int y) {
		if (!The5zigMod.getNetworkManager().isConnected()) {
			The5zigMod.getVars().drawString(ChatColor.RED + I18n.translate("connection.offline"), x + xOff + 2, y + yOff + 2);
			return;
		}
		Object[] values = new Object[containers.length];
		for (int i = 0; i < containers.length; i++) {
			values[i] = containers[i].getValue();
		}
		The5zigMod.getVars().drawString(I18n.translate(translation, values), x + xOff + 2, y + yOff + 2);
	}
}
