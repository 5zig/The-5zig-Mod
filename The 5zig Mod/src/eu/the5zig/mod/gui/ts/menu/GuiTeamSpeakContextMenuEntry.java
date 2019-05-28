package eu.the5zig.mod.gui.ts.menu;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.ts.entries.GuiTeamSpeakEntry;
import eu.the5zig.mod.util.Vector2i;

public abstract class GuiTeamSpeakContextMenuEntry<T extends GuiTeamSpeakEntry> {

	private Vector2i iconUV;
	private String translationKey;

	public GuiTeamSpeakContextMenuEntry(Vector2i iconUV, String translationKey) {
		this.iconUV = iconUV;
		this.translationKey = translationKey;
	}

	public abstract void onClick(T entry);

	protected boolean isSelected() {
		return false;
	}

	public void draw(int x, int y) {
		if (isSelected()) {
			Gui.drawRect(x, y, 10, 10, 0x885599fa);
		}
		The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(I18n.translate("teamspeak.menu." + translationKey), 150 - 18), x + 16, y);
	}

	public void drawIcon(int x, int y) {
		if (iconUV != null) {
			Gui.drawModalRectWithCustomSizedTexture(x + 2, y, iconUV.getX() * 128 / 12, iconUV.getY() * 128 / 12, 128 / 12, 128 / 12, 2048 / 12, 2048 / 12);
		}
	}

}
