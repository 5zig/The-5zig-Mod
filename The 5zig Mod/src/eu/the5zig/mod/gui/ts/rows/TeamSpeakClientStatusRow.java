package eu.the5zig.mod.gui.ts.rows;

import eu.the5zig.mod.I18n;
import eu.the5zig.mod.The5zigMod;
import eu.the5zig.mod.gui.Gui;
import eu.the5zig.mod.gui.elements.Row;
import eu.the5zig.teamspeak.api.Client;

public class TeamSpeakClientStatusRow implements Row {

	private int width;
	private Client client;

	public TeamSpeakClientStatusRow(int width, Client client) {
		this.width = width;
		this.client = client;
	}

	@Override
	public void draw(int x, int y) {
		x += 2;
		y += 2;
		int startY = y;

		// icons
		The5zigMod.getVars().bindTexture(The5zigMod.TEAMSPEAK_ICONS);
		if (client.isAway()) {
			drawSprite(x, y, 10, 0);
			y += 16;
		}
		if (!client.hasOutputHardware()) {
			drawSprite(x, y, 12, 4);
			y += 16;
		}
		if (!client.hasInputHardware()) {
			drawSprite(x, y, 11, 4);
			y += 16;
		}
		if (client.isOutputMuted()) {
			drawSprite(x, y, 8, 6);
			y += 16;
		}
		if (client.isInputMuted()) {
			drawSprite(x, y, 6, 5);
		}

		// text
		y = startY;
		if (client.isAway()) {
			The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(I18n.translate("teamspeak.entry.client.away"), width - 20), x + 18, y + 4);
			y += 16;
		}
		if (!client.hasOutputHardware()) {
			The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(I18n.translate("teamspeak.entry.client.output_deactivated"), width - 20), x + 18, y + 4);
			y += 16;
		}
		if (!client.hasInputHardware()) {
			The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(I18n.translate("teamspeak.entry.client.input_deactivated"), width - 20), x + 18, y + 4);
			y += 16;
		}
		if (client.isOutputMuted()) {
			The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(I18n.translate("teamspeak.entry.client.output_muted"), width - 20), x + 18, y + 4);
			y += 16;
		}
		if (client.isInputMuted()) {
			The5zigMod.getVars().drawString(The5zigMod.getVars().shortenToWidth(I18n.translate("teamspeak.entry.client.input_muted"), width - 20), x + 18, y + 4);
		}
	}

	private void drawSprite(int x, int y, int u, int v) {
		Gui.drawModalRectWithCustomSizedTexture(x, y, u * 128 / 10, v * 128 / 10, 128 / 10, 128 / 10, 2048 / 10, 2048 / 10);
	}

	@Override
	public int getLineHeight() {
		int height = 0;
		if (client.isAway()) {
			height += 16;
		}
		if (!client.hasOutputHardware()) {
			height += 16;
		}
		if (!client.hasInputHardware()) {
			height += 16;
		}
		if (client.isOutputMuted()) {
			height += 16;
		}
		if (client.isInputMuted()) {
			height += 16;
		}
		return height;
	}
}
